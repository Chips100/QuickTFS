package quicktfs.apiclients.restapi.Authentication;

import com.squareup.okhttp.OkHttpClient;

/**
 * Provides login data for accessing the TFS Rest API.
 */
public interface AuthenticationState {
    /**
     * Configures the authentication for the specified client.
     * @param client Client that will be used for accessing the TFS Rest API.
     */
    void configureClientAuthentication(OkHttpClient client);

    /**
     * Changes the credentials that should be used for authentication.
     * This resets the current identity until setLoggedIn is called.
     * @param username Username to use for authentication.
     * @param password Password to use for authentication.
     */
    void changeCredentials(String username, String password);

    /**
     * Specifies that the credentials have been used for a successful login.
     * @param identity Identity that was authenticated by the credentials.
     */
    void setLoggedIn(AuthenticatedIdentity identity);

    /**
     * Specifies if this authentication is logged in.
     * @return True, if this authentication is logged in and can be used for non-anonymous access; otherwise false.
     */
    boolean isLoggedIn();

    /**
     * Gets the identity that this authentication is logged in with.
     * @return The identity of the current login; or null if not logged in.
     */
    AuthenticatedIdentity getIdentity();
}