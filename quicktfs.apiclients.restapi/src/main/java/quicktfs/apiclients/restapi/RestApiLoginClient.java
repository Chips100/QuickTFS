package quicktfs.apiclients.restapi;

import android.widget.Toast;

import quicktfs.apiclients.contracts.LoginClient;

/**
 * Client that allows logging in to a TFS Api using the HTTP Rest API.
 */
public class RestApiLoginClient extends RestApiClientBase implements LoginClient {
    /**
     * Creates a RestApiLoginClient.
     * @param restApiUrl The URL of the TFS HTTP Rest API.
     */
    public RestApiLoginClient(String restApiUrl) {
        super(restApiUrl);
    }

    /**
     * Tries logging in with the specified credentials.
     * @param username UserName of the account to use for logging in.
     * @param password Password of the account to use for logging in.
     * @return True, if the login was successful; otherwise false.
     */
    @Override
    public boolean tryLogin(String username, String password) {
        return false;
    }
}