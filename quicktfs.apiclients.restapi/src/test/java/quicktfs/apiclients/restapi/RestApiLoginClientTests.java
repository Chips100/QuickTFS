package quicktfs.apiclients.restapi;

import org.junit.Test;

import quicktfs.apiclients.contracts.exceptions.ApiAccessException;
import quicktfs.apiclients.restapi.authentication.AuthenticatedIdentity;
import quicktfs.apiclients.restapi.authentication.AuthenticationFailedException;

import static org.junit.Assert.*;

/**
 * Tests for the RestApiLoginClient.
 */
public class RestApiLoginClientTests {
    /**
     * tryLogin should use the specified credentials to login
     * and remember the logged in identity from the login.
     */
    @Test
    public void RestApiLoginClient_tryLogin_success() throws Exception {
        // Create client that checks if the login parameters are correct.
        final RestClientMock restClientMock = new RestClientMock();
        restClientMock.configureGetBehaviour(new RestClientMock.GetBehaviour() {
            @Override
            public <T> T get(String apiUrl, Class<T> responseClass) throws ApiAccessException {
                // Fail if any credential parameter is wrong.
                if (restClientMock.getCredentials() == null
                        || !restClientMock.getCredentials().getDomain().equals("domain")
                        || !restClientMock.getCredentials().getUsername().equals("username")
                        || !restClientMock.getCredentials().getPassword().equals("password")) {
                    throw new ApiAccessException("Wrong credentials");
                }

                if (!apiUrl.equals("_api/_common/GetUserProfile?__v=5")) {
                    throw new ApiAccessException("Wrong Login URL");
                }

                if (responseClass != RestApiLoginClient.LoginResponse.class) {
                    throw new ApiAccessException("Wrong expected Response class");
                }

                // Mock an identity to return to the RestApiLoginClient.
                RestApiLoginClient.LoginResponse response = new RestApiLoginClient.LoginResponse();
                response.identity = new RestApiLoginClient.LoginIdentity();
                response.identity.Domain = "IdentityDomain";
                response.identity.AccountName = "IdentityAccountName";
                response.identity.DisplayName = "IdentityDisplayName";
                response.identity.MailAddress = "IdentityMailAddress";
                return (T)response;
            }
        });

        RestApiLoginClient sut = new RestApiLoginClient(restClientMock);
        boolean loginResult = sut.tryLogin("domain", "username", "password");
        assertEquals(true, loginResult);

        // Check if the identity has been remembered.
        AuthenticatedIdentity identity = restClientMock.getCurrentIdentity();
        assertNotNull(identity);
        assertEquals("IdentityDomain", identity.getDomain());
        assertEquals("IdentityDisplayName", identity.getDisplayName());
        assertEquals("IdentityAccountName", identity.getAccountName());
        assertEquals("IdentityMailAddress", identity.getMailAddress());
    }

    /**
     * tryLogin should return false if login fails
     * due to invalid credentials (as indicated by an AuthenticationFailedException).
     */
    @Test
    public void RestApiLoginClient_tryLogin_AuthenticationFailed() throws Exception {
        // Create client that throws AuthenticationFailedException on GET request.
        RestClientMock restClientMock = new RestClientMock();
        restClientMock.configureGetBehaviour(new RestClientMock.GetBehaviour() {
            @Override
            public <T> T get(String apiUrl, Class<T> responseClass) throws ApiAccessException {
                throw new ApiAccessException(new AuthenticationFailedException());
            }
        });

        RestApiLoginClient sut = new RestApiLoginClient(restClientMock);
        boolean loginResult = sut.tryLogin("domain", "username", "password");
        assertEquals(false, loginResult);
    }

    /**
     * tryLogin should rethrow exceptions thrown by the RestClient.
     */
    @Test(expected = ApiAccessException.class)
    public void RestApiLoginClient_tryLogin_OtherException() throws Exception {
        // Create client that throws exception on GET request.
        RestClientMock restClientMock = new RestClientMock();
        restClientMock.configureGetBehaviour(new RestClientMock.GetBehaviour() {
            @Override
            public <T> T get(String apiUrl, Class<T> responseClass) throws ApiAccessException {
                throw new ApiAccessException("UnitTest.", null);
            }
        });

        RestApiLoginClient sut = new RestApiLoginClient(restClientMock);
        sut.tryLogin("domain", "username", "password");
    }
}