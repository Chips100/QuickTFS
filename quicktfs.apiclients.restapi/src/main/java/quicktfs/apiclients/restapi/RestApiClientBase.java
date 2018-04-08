package quicktfs.apiclients.restapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.net.HttpURLConnection;
import java.util.HashSet;
import java.util.Set;

import quicktfs.apiclients.contracts.ApiAccessException;
import quicktfs.apiclients.contracts.ConfigurationSource;
import quicktfs.apiclients.contracts.EntityNotFoundException;
import quicktfs.apiclients.contracts.NotAuthenticatedException;
import quicktfs.apiclients.restapi.Authentication.AuthenticationState;
import quicktfs.apiclients.restapi.SSL.CustomSSLCertificates;
import quicktfs.utilities.StringUtilities;

/**
 * Base class for Clients using the TFS HTTP Rest API.
 */
public abstract class RestApiClientBase {
    private final Gson gson = new GsonBuilder().create();
    private final ConfigurationSource configurationSource;
    private final AuthenticationState authentication;

    /**
     * Creates a RestApiLoginClient.
     * @param configurationSource Configuration source for looking up configuration settings.
     * @param authentication Authentication for the TFS HTTP Rest API.
     */
    protected RestApiClientBase(ConfigurationSource configurationSource, AuthenticationState authentication) {
        if (configurationSource == null) throw new IllegalArgumentException("configurationSource");
        if (authentication == null) throw new IllegalArgumentException("authentication");

        this.configurationSource = configurationSource;
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
                .url(buildAbsoluteApiUrl(apiUrl))
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
            .url(buildAbsoluteApiUrl(apiUrl))
            .patch(RequestBody.create(MediaType.parse("application/json-patch+json"), gson.toJson(body)))
            .build(), responseClass);
    }

    private <T> T executeRequest(Request request, Class<T> responseClass) throws ApiAccessException {
        try {
            OkHttpClient client = createHttpClient();
            Call call = client.newCall(request);
            Response response = call.execute();

            if (!response.isSuccessful()) {
                handleFailedResponse(response);
            }

            String responseBody = response.body().string();
            return gson.fromJson(responseBody, responseClass);
        }
        catch(ApiAccessException exception) {
            throw exception;
        }
        catch(Exception exception) {
            throw new ApiAccessException(exception);
        }
    }

    private void handleFailedResponse(Response response) throws ApiAccessException {
        if (response.code() == HttpURLConnection.HTTP_NOT_FOUND) {
            throw new EntityNotFoundException();
        }

        throw new ApiAccessException("Server response indicates an error.", null);
    }

    private OkHttpClient createHttpClient()
            throws CustomSSLCertificates.CustomSSLCertificatesException {
        OkHttpClient client = new OkHttpClient();
        authentication.configureClientAuthentication(client);

        Set<String> certificateStrings = configurationSource.getStringSet("TRUSTED_CERTIFICATES");
        if (certificateStrings == null) certificateStrings = new HashSet<>();

        client.setSslSocketFactory(
            CustomSSLCertificates.createSSLSocketFactoryWithTrustedCertificates(
                certificateStrings.toArray(new String[certificateStrings.size()])));

        return client;
    }

    private String buildAbsoluteApiUrl(String relativeApiUrl) throws ApiAccessException {
        String tfsUrl = configurationSource.getString("TFS_URL");
        if (StringUtilities.isNullOrEmpty(tfsUrl)) {
            throw new ApiAccessException("TFS Url not configured.", null);
        }

        return tfsUrl + relativeApiUrl;
    }
}