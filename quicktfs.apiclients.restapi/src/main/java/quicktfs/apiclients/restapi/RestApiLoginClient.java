package quicktfs.apiclients.restapi;

import quicktfs.apiclients.contracts.ApiAccessException;
import quicktfs.apiclients.contracts.LoginClient;
import quicktfs.apiclients.restapi.Authentication.AuthenticatedIdentity;
import quicktfs.apiclients.restapi.Authentication.AuthenticationState;
import quicktfs.apiclients.restapi.NTLM.NTLMAuthenticationException;
import quicktfs.utilities.ExceptionUtilities;

/**
 * Client that allows logging in to a TFS Api using the HTTP Rest API.
 */
public class RestApiLoginClient extends RestApiClientBase implements LoginClient {
    /**
     * Creates a RestApiLoginClient.
     * @param restApiUrl The URL of the TFS HTTP Rest API.
     * @param authentication Authentication for the TFS HTTP Rest API.
     */
    public RestApiLoginClient(String restApiUrl, AuthenticationState authentication) {
        super(restApiUrl, authentication);
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
            this.getAuthentication().changeCredentials(domain, username, password);
            LoginResponse response = callApiGet("_api/_common/GetUserProfile?__v=5", LoginResponse.class);

            // If login was successful, remember the logged in identity.
            this.getAuthentication().setLoggedIn(new AuthenticatedIdentity(
                    response.identity.Domain,
                    response.identity.AccountName,
                    response.identity.DisplayName,
                    response.identity.MailAddress
            ));

            return true;
        } catch (ApiAccessException exception) {
            // If the exception is ultimately caused by an NTLMAuthenticationException,
            // there is an error in the credentials - return this as a failed login.
            if (ExceptionUtilities.findCauseOfType(exception, NTLMAuthenticationException.class) != null) {
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