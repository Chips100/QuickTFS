package quicktfs.apiclients.restapi;

import com.squareup.okhttp.OkHttpClient;

import quicktfs.apiclients.contracts.ApiAccessException;
import quicktfs.apiclients.contracts.LoginClient;
import quicktfs.apiclients.restapi.NTLM.NTLMAuthenticationException;
import quicktfs.apiclients.restapi.NTLM.NTLMAuthenticator;
import quicktfs.utilities.ExceptionUtilities;

/**
 * Client that allows logging in to a TFS Api using the HTTP Rest API.
 */
public class RestApiLoginClient extends RestApiClientBase implements LoginClient, RestApiLogin {
    private String domain = "DPAORINP";
    private String username;
    private String password;

    /**
     * Creates a RestApiLoginClient.
     * @param restApiUrl The URL of the TFS HTTP Rest API.
     */
    public RestApiLoginClient(String restApiUrl) {
        super(restApiUrl);
    }

    /**
     * Should be overridden to return a component
     * that provides login data to the API calls.
     * @return An instance of RestApiLogin to provide login data to the API calls.
     */
    @Override
    protected RestApiLogin getLogin() {
        // Return itself to test access with the current login data.
        return this;
    }

    /**
     * Tries logging in with the specified credentials.
     * @param username UserName of the account to use for logging in.
     * @param password Password of the account to use for logging in.
     * @return True, if the login was successful; otherwise false.
     */
    @Override
    public boolean tryLogin(String username, String password) throws ApiAccessException {
        this.username = username;
        this.password = password;

        try {
            // Try an API call with the specified credentials.
            callApiGet("projects?stateFilter=All&api-version=1.0");
            return true;
        }
        catch(ApiAccessException exception) {
            // If the exception is ultimately caused by an NTLMAuthenticationException,
            // there is an error in the credentials - return this as a failed login.
            if (ExceptionUtilities.findCauseOfType(exception, NTLMAuthenticationException.class) != null) {
                return false;
            }

            // Otherwise there is a different underlying error (e.g. network); pass on.
            throw exception;
        }
    }

    @Override
    public void configureLogin(OkHttpClient client) {
        client.setAuthenticator(new NTLMAuthenticator(username, password, domain));
    }
}