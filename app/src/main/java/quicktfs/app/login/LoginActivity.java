package quicktfs.app.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import quicktfs.apiclients.contracts.LoginClient;
import quicktfs.apiclients.restapi.RestApiLoginClient;
import quicktfs.app.IocContainerStub;
import quicktfs.app.R;
import quicktfs.app.workItemDetails.WorkItemDetailsActivity;

/**
 * A login screen that offers login via User Name / Password.
 */
public class LoginActivity extends AppCompatActivity {
    private LoginClient loginClient;

    // UI references.
    private EditText userNameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginClient = IocContainerStub.getDefaultInstance().getInstance(LoginClient.class);

        // Set up the login form.
        userNameEditText = (EditText) findViewById(R.id.loginUserName);
        passwordEditText = (EditText) findViewById(R.id.loginPassword);
        loginButton = (Button)findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.loginProgressBar);
    }

    public void login(View view) {
        String username = userNameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        setLoadingState(true);
        new AsyncLoginTask(loginClient) {
            @Override
            protected void onPostExecute(LoginResult result) {
                LoginActivity context = LoginActivity.this;
                context.setLoadingState(false);

                if (!result.wasSuccessful()) {
                    Toast.makeText(context, context.getString(R.string.loginFailedMessage), Toast.LENGTH_LONG).show();
                    return;
                }

                context.onLoginSuccess();
            }
        }.execute(new AsyncLoginTask.LoginParams(username, password));
    }

    private void onLoginSuccess() {
        Intent intent = new Intent(this, WorkItemDetailsActivity.class);
        intent.putExtra(WorkItemDetailsActivity.INTENT_WORKITEM_ID, 55);
        startActivity(intent);
    }

    private void setLoadingState(boolean isLoading) {
        loginButton.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        setEditTextLoadingState(userNameEditText, isLoading);
        setEditTextLoadingState(passwordEditText, isLoading);
    }

    private void setEditTextLoadingState(EditText editText, boolean isLoading) {
        editText.setEnabled(!isLoading);
        editText.setFocusable(!isLoading);

        // Required to make elements focusable again after disabling focus.
        // https://stackoverflow.com/questions/7407851/edittext-setfocusablefalse-cant-be-set-to-true
        if (!isLoading) {
            editText.setFocusableInTouchMode(true);
        }
    }
}