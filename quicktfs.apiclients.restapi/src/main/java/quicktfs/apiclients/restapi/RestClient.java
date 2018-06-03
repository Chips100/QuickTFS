package quicktfs.apiclients.restapi;

import quicktfs.apiclients.contracts.exceptions.ApiAccessException;
import quicktfs.apiclients.restapi.authentication.AuthenticatedIdentity;

/**
 * Client for accessing a REST interface.
 */
public interface RestClient {
    /**
     * Executes a GET call to the REST interface.
     * @param apiUrl Relative URL of the REST action that should be called.
     * @param responseClass Type of the response for deserialization.
     * @param <T> Type of the response.
     * @return The result returned from the Api.
     * @throws ApiAccessException Thrown if any errors occur while accessing the Api.
     */
    <T> T get(String apiUrl, Class<T> responseClass) throws ApiAccessException;

    /**
     * Executes a PATCH call to the REST interface.
     * @param apiUrl Relative URL of the REST action that should be called.
     * @param body Body that will be sent with the patch request.
     * @param responseClass Type of the response for deserialization.
     * @param <TBody> Type of the body to send with the patch request.
     * @param <TResponse> Type of the response.
     * @return The result returned from the Api.
     * @throws ApiAccessException Thrown if any errors occur while accessing the Api.
     */
    <TBody, TResponse> TResponse patch(String apiUrl, TBody body, Class<TResponse> responseClass)
            throws ApiAccessException;

    /**
     * Sets the credentials that should be used when accessing the REST interface.
     * @param domain Name of the domain.
     * @param username Name of the user.
     * @param password Password of the account.
     */
    void setCredentials(String domain, String username, String password);

    /**
     * Sets the current identity that has been received
     * after successfully logging in with previously configured credentials.
     * @param identity Identity that is currently logged in.
     */
    void setCurrentIdentity(AuthenticatedIdentity identity);

    /**
     * Gets the identity that is currently logged in; or null if not logged in.
     * @return The identity that is currently logged in; or null if not logged in.
     */
    AuthenticatedIdentity getCurrentIdentity();
}
