package quicktfs.app.login;

import android.os.AsyncTask;

import quicktfs.apiclients.contracts.LoginClient;

/**
 * A task for performing a login asynchronously.
 */
abstract class AsyncLoginTask extends AsyncTask<AsyncLoginTask.LoginParams, Void, AsyncLoginTask.LoginResult> {
    private final LoginClient client;

    /**
     * Creates an AsyncLoginTask.
     * @param client Client to use for logging in.
     */
    AsyncLoginTask(LoginClient client) {
        this.client = client;
    }

    @Override
    protected LoginResult doInBackground(LoginParams... loginParams) {
        if (loginParams == null || loginParams.length != 1) {
            throw new IllegalArgumentException("loginParams");
        }

        LoginParams params = loginParams[0];
        boolean result = client.tryLogin(params.getUsername(), params.getPassword());
        return new LoginResult(result);
    }

    /**
     * Should be overridden to define the handling of a completed login.
     * @param result Result of the login operation.
     */
    @Override
    protected abstract void onPostExecute(LoginResult result);


    /**
     * Represents parameters for performing a login.
     */
    static class LoginParams {
        private final String username;
        private final String password;

        /**
         * Creates LoginParams.
         * @param username Username to use for logging in.
         * @param password Password to use for logging in.
         */
        LoginParams(String username, String password) {
            this.username = username;
            this.password = password;
        }

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