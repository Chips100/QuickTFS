package quicktfs.apiclients.restapi;

import quicktfs.apiclients.contracts.exceptions.ApiAccessException;
import quicktfs.apiclients.contracts.LoginClient;
import quicktfs.apiclients.restapi.authentication.AuthenticatedIdentity;
import quicktfs.apiclients.restapi.authentication.AuthenticationFailedException;
import quicktfs.utilities.ExceptionUtilities;

/**
 * Client that allows logging in to a TFS Api using the HTTP Rest API.
 */
public class RestApiLoginClient implements LoginClient {
    private final RestClient client;

    /**
     * Creates a RestApiLoginClient.
     * @param client Client used for communication with the REST interface.
     */
    public RestApiLoginClient(RestClient client) {
        this.client = client;
    }

    /**
     * Tries logging in with the specified credentials.
     * @param domain Domain of the account to use for logging in.
     * @param username UserName of the account to use for logging in.
     * @param password Password of the account to use for logging in.
     * @return True, if the login was successful; otherwise false.
     */
    @Override
    public boolean tryLogin(String domain, String username, String password) throws ApiAccessException {
        try {
            // Try an API call with the specified credentials.
            client.setCredentials(domain, username, password);
            LoginResponse response = client.get("_api/_common/GetUserProfile?__v=5", LoginResponse.class);

            // If login was successful, remember the logged in identity.
            client.setCurrentIdentity(new AuthenticatedIdentity(
                    response.identity.Domain,
                    response.identity.AccountName,
                    response.identity.DisplayName,
                    response.identity.MailAddress
            ));

            return true;
        } catch (ApiAccessException exception) {
            // If the exception is ultimately caused by an NTLMAuthenticationException,
            // there is an error in the credentials - return this as a failed login.
            if (ExceptionUtilities.findCauseOfType(exception, AuthenticationFailedException.class) != null) {
                return false;
            }

            // Otherwise there is a different underlying error (e.g. network); pass on.
            throw exception;
        }
    }

    public static class LoginResponse {
        public LoginIdentity identity;
    }

    public static class LoginIdentity {
        public String Domain;
        public String AccountName;
        public String DisplayName;
        public String MailAddress;
    }
}