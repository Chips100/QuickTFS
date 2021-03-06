package quicktfs.app.login;

import quicktfs.apiclients.contracts.exceptions.ApiAccessException;
import quicktfs.apiclients.contracts.LoginClient;
import quicktfs.app.AsyncApiClientTask;

/**
 * A task for performing a login asynchronously.
 */
abstract class AsyncLoginTask extends AsyncApiClientTask<LoginActivity, AsyncLoginTask.LoginParams, AsyncLoginTask.LoginResult> {
    private final LoginClient client;

    /**
     * Creates an AsyncLoginTask.
     * @param context Context from which the task is started.
     * @param client Client to use for logging in.
     */
    AsyncLoginTask(LoginActivity context, LoginClient client) {
        super(context);
        this.client = client;
    }

    @Override
    protected LoginResult doApiAction(LoginParams loginParams) throws ApiAccessException {
        boolean result = client.tryLogin(loginParams.getDomain(), loginParams.getUsername(), loginParams.getPassword());
        return new LoginResult(result);
    }

    /**
     * Represents parameters for performing a login.
     */
    static class LoginParams {
        private final String domain;
        private final String username;
        private final String password;

        /**
         * Creates LoginParams.
         * @param domain Domain to use for logging in.
         * @param username Username to use for logging in.
         * @param password Password to use for logging in.
         */
        LoginParams(String domain, String username, String password) {
            this.domain = domain;
            this.username = username;
            this.password = password;
        }

        /**
         * Gets the domain to use for logging in.
         * @return The domain to use for logging in.
         */
        String getDomain() { return domain; }

        /**
         * Gets the Username to use for logging in.
         * @return The Username to use for logging in.
         */
        String getUsername() {
            return username;
        }

        /**
         * Gets the Password to use for logging in.
         * @return The Password to use for logging in.
         */
        String getPassword() {
            return password;
        }
    }

    /**
     * Represents the result of a login operation.
     */
    static class LoginResult {
        private final boolean success;

        /**
         * Creates a LoginResult.
         * @param success True, if the login has been successful; otherwise false.
         */
        LoginResult(boolean success) {
            this.success = success;
        }

        /**
         * Indicates if the login has been successful.
         * @return True, if the login has been successful; otherwise false.
         */
        boolean wasSuccessful() {
            return success;
        }
    }
}