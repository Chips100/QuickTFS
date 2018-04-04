package quicktfs.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A login screen that offers login via User Name / Password.
 */
public class LoginActivity extends AppCompatActivity {
    // UI references.
    private EditText userNameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        userNameEditText = (EditText) findViewById(R.id.loginUserName);
        passwordEditText = (EditText) findViewById(R.id.loginPassword);
        loginButton = (Button)findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.loginProgressBar);
    }

    public void login(View view) {
        String userName = userNameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        Toast.makeText(this, "Login mit: " + userName + "/" + password, Toast.LENGTH_LONG).show();
        setLoadingState(true);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        setLoadingState(false);
                    }
                },
                3000);
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
    }
}
