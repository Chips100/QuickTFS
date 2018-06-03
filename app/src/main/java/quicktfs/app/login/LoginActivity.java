package quicktfs.app.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import quicktfs.apiclients.contracts.LoginClient;
import quicktfs.app.AppConfigurationSource;
import quicktfs.app.IocContainerStub;
import quicktfs.app.R;
import quicktfs.app.home.HomeActivity;
import quicktfs.app.settings.SettingsActivity;
import quicktfs.utilities.UiUtilities;

/**
 * A login screen that offers login via User Name / Password.
 */
public class LoginActivity extends AppCompatActivity {
    private LoginClient loginClient;
    private LoginStore loginStore;

    // UI references.
    private EditText domainEditText;
    private EditText userNameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginStore = new LoginStore(this);
        AppConfigurationSource.getInstance().init(this);
        loginClient = IocContainerStub.getInstance(LoginClient.class);

        // Set up the login form.
        domainEditText = findViewById(R.id.loginDomain);
        userNameEditText = findViewById(R.id.loginUserName);
        passwordEditText = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.loginProgressBar);

        // Restore remembered login data.
        domainEditText.setText(loginStore.getDomain());
        userNameEditText.setText(loginStore.getUsername());
        passwordEditText.setText(loginStore.getPassword());
        UiUtilities.focusFirstEmptyEditText(domainEditText, userNameEditText, passwordEditText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuSettings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void login(View view) {
        final AsyncLoginTask.LoginParams loginParams = new AsyncLoginTask.LoginParams(
            domainEditText.getText().toString(),
            userNameEditText.getText().toString(),
            passwordEditText.getText().toString()
        );

        setLoadingState(true);
        new AsyncLoginTask(this, loginClient) {
            @Override
            protected void handleComplete() {
                LoginActivity.this.setLoadingState(false);
            }

            @Override
            protected void handleSuccess(LoginResult result) {
                LoginActivity context = LoginActivity.this;

                if (!result.wasSuccessful()) {
                    Toast.makeText(context, context.getString(R.string.loginFailedMessage), Toast.LENGTH_LONG).show();
                    return;
                }

                loginStore.storeLoginData(loginParams.getDomain(), loginParams.getUsername(), loginParams.getPassword());
                context.onLoginSuccess();
            }
        }.execute(loginParams);
    }

    private void onLoginSuccess() {
        startActivity(new Intent(this, HomeActivity.class));
    }

    private void setLoadingState(boolean isLoading) {
        loginButton.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        setEditTextLoadingState(userNameEditText, isLoading);
        setEditTextLoadingState(passwordEditText, isLoading);
        setEditTextLoadingState(domainEditText, isLoading);
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