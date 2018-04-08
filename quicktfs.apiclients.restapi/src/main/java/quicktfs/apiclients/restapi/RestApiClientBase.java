package quicktfs.apiclients.restapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import quicktfs.apiclients.contracts.ApiAccessException;
import quicktfs.apiclients.contracts.NotAuthenticatedException;
import quicktfs.apiclients.restapi.Authentication.AuthenticationState;
import quicktfs.apiclients.restapi.SSL.CustomSSLCertificates;

/**
 * Base class for Clients using the TFS HTTP Rest API.
 */
public abstract class RestApiClientBase {
    private final Gson gson = new GsonBuilder().create();
    private final String restApiUrl;
    private final AuthenticationState authentication;

    /**
     * Creates a RestApiLoginClient.
     * @param restApiUrl The URL of the TFS HTTP Rest API.
     * @param authentication Authentication for the TFS HTTP Rest API.
     */
    protected RestApiClientBase(String restApiUrl, AuthenticationState authentication) {
        if (restApiUrl == null || restApiUrl.equals("")) throw new IllegalArgumentException("restApiUrl");
        if (authentication == null) throw new IllegalArgumentException("authentication");

        this.restApiUrl = restApiUrl;
        this.authentication = authentication;
    }

    /**
     * Gets the authentication with information about current login state.
     * @return The authentication with information about current login state.
     */
    protected AuthenticationState getAuthentication() { return authentication; }

    /**
     * Ensures that the current AuthenticationState is logged in.
     * @throws NotAuthenticatedException Thrown if the current AuthenticationState is not logged in.
     */
    protected void ensureLoggedIn() throws NotAuthenticatedException {
        if (!authentication.isLoggedIn()) {
            throw new NotAuthenticatedException();
        }
    }

    /**
     * Makes an HTTP GET Api call.
     * @param apiUrl Relative URL of the REST Api action that should be called.
     * @param responseClass Type of the response for deserialization.
     * @param <T> Type of the response.
     * @return The result returned from the Api.
     * @throws ApiAccessException Thrown if any errors occur by accessing the Api.
     */
    protected <T> T callApiGet(String apiUrl, Class<T> responseClass) throws ApiAccessException {
        return executeRequest(new Request.Builder()
                .url(restApiUrl + apiUrl)
                .get()
                .build(), responseClass);
    }

    /**
     * Makes an HTTP PATCH Api call with the specified body.
     * @param apiUrl Relative URL of the REST Api action that should be called.
     * @param body Body that will be send with the patch request.
     * @param responseClass Type of the response for deserialization.
     * @param <TBody> Type of the body to send with the patch request.
     * @param <TResponse> Type of the response.
     * @return The result returned from the Api.
     * @throws ApiAccessException Thrown if any errors occur by accessing the Api.
     */
    protected <TBody, TResponse> TResponse callApiPatch(String apiUrl, TBody body, Class<TResponse> responseClass)
            throws ApiAccessException {
        return executeRequest(new Request.Builder()
            .url(restApiUrl + apiUrl)
            .patch(RequestBody.create(MediaType.parse("application/json-patch+json"), gson.toJson(body)))
            .build(), responseClass);
    }

    private <T> T executeRequest(Request request, Class<T> responseClass) throws ApiAccessException {
        try {
            OkHttpClient client = createHttpClient();
            Call call = client.newCall(request);
            Response response = call.execute();
            String responseBody = response.body().string();

            return gson.fromJson(responseBody, responseClass);
        }
        catch(Exception exception) {
            throw new ApiAccessException(exception);
        }
    }

    private OkHttpClient createHttpClient()
            throws CustomSSLCertificates.CustomSSLCertificatesException {
        OkHttpClient client = new OkHttpClient();
        authentication.configureClientAuthentication(client);
        client.setSslSocketFactory(CustomSSLCertificates.createSSLSocketFactoryWithTrustedCertificates(CustomSSLCertificates.DataportCertificate));
        return client;
    }
}