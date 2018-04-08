package quicktfs.apiclients.restapi.Authentication;

import com.squareup.okhttp.OkHttpClient;

import quicktfs.apiclients.restapi.NTLM.NTLMAuthenticator;

/**
 * Provides login data for the TFS HTTP Rest API with NTLM authentication.
 */
public class RestApiAuthenticationState implements AuthenticationState {
    private String domain;
    private String username;
    private String password;
    private AuthenticatedIdentity identity;

    /**
     * Changes the credentials that should be used for authentication.
     * This resets the current identity until setLoggedIn is called.
     * @param domain Domain to use for authentication.
     * @param username Username to use for authentication.
     * @param password Password to use for authentication.
     */
    @Override
    public void changeCredentials(String domain, String username, String password) {
        this.domain = domain;
        this.username = username;
        this.password = password;
        this.identity = null;
    }

    /**
     * Specifies that the credentials have been used for a successful login.
     * @param identity Identity that was authenticated by the credentials.
     */
    @Override
    public void setLoggedIn(AuthenticatedIdentity identity) {
        this.identity = identity;
    }

    /**
     * Specifies if this authentication is logged in.
     * @return True, if this authentication is logged in and can be used for non-anonymous access; otherwise false.
     */
    @Override
    public boolean isLoggedIn() { return this.identity != null; }

    /**
     * Gets the identity that this authentication is logged in with.
     * @return The identity of the current login; or null if not logged in.
     */
    @Override
    public AuthenticatedIdentity getIdentity() { return this.identity; }

    /**
     * Configures the login data for the specified client.
     * @param client Client that will be used for accessing the TFS Rest API.
     */
    @Override
    public void configureClientAuthentication(OkHttpClient client) {
        client.setAuthenticator(new NTLMAuthenticator(username, password, domain));
    }
}