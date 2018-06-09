package quicktfs.apiclients.restapi.RestClientImpl;

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

import quicktfs.apiclients.contracts.ConfigurationSource;
import quicktfs.apiclients.contracts.exceptions.ApiAccessException;
import quicktfs.apiclients.contracts.exceptions.EntityNotFoundException;
import quicktfs.apiclients.restapi.RestClient;
import quicktfs.apiclients.restapi.RestClientImpl.NTLM.NTLMAuthenticator;
import quicktfs.apiclients.restapi.RestClientImpl.SSL.CustomSSLCertificates;
import quicktfs.apiclients.restapi.authentication.AuthenticatedIdentity;
import quicktfs.utilities.StringUtilities;

/**
 * Implementation to fulfill the interface defined for communication
 * with the REST interface.
 *
 * Basically, this class just puts together some third-party service
 * and deals with some low-level details (NTLM authentication, JSON serialization,
 * HTTP communication) that are difficult to test and will be mocked in tests.
 */
public final class RestClientImpl implements RestClient {
    private final Gson gson = new GsonBuilder().create();
    private final ConfigurationSource configurationSource;
    private final OkHttpClient httpClient = new OkHttpClient();
    private AuthenticatedIdentity identity;

    /**
     * Creates a RestApiLoginClient.
     * @param configurationSource Configuration source for looking up configuration settings.
     */
    public RestClientImpl(ConfigurationSource configurationSource) {
        if (configurationSource == null) throw new IllegalArgumentException("configurationSource");

        this.configurationSource = configurationSource;
    }

    /**
     * Executes a GET call to the REST interface.
     *
     * @param apiUrl        Relative URL of the REST action that should be called.
     * @param responseClass Type of the response for deserialization.
     * @return The result returned from the Api.
     * @throws ApiAccessException Thrown if any errors occur while accessing the Api.
     */
    @Override
    public <T> T get(String apiUrl, Class<T> responseClass) throws ApiAccessException {
        return executeRequest(new Request.Builder()
                .url(buildAbsoluteApiUrl(apiUrl))
                .get()
                .build(), responseClass);
    }

    /**
     * Executes a PATCH call to the REST interface.
     *
     * @param apiUrl         Relative URL of the REST action that should be called.
     * @param body           Body that will be sent with the patch request.
     * @param responseClass  Type of the response for deserialization.
     * @return The result returned from the Api.
     * @throws ApiAccessException Thrown if any errors occur while accessing the Api.
     */
    @Override
    public <TBody, TResponse> TResponse patch(String apiUrl, TBody body, Class<TResponse> responseClass) throws ApiAccessException {
        return executeRequest(new Request.Builder()
                .url(buildAbsoluteApiUrl(apiUrl))
                .patch(RequestBody.create(MediaType.parse("application/json-patch+json"), gson.toJson(body)))
                .build(), responseClass);
    }

    /**
     * Sets the credentials that should be used when accessing the REST interface.
     *
     * @param domain   Name of the domain.
     * @param username Name of the user.
     * @param password Password of the account.
     */
    @Override
    public void setCredentials(String domain, String username, String password) {
        this.httpClient.setAuthenticator(new NTLMAuthenticator(username, password, domain.toUpperCase()));
    }

    /**
     * Sets the current identity that has been received
     * after successfully logging in with previously configured credentials.
     *
     * @param identity Identity that is currently logged in.
     */
    @Override
    public void setCurrentIdentity(AuthenticatedIdentity identity) {
        this.identity = identity;
    }

    /**
     * Gets the identity that is currently logged in; or null if not logged in.
     *
     * @return The identity that is currently logged in; or null if not logged in.
     */
    @Override
    public AuthenticatedIdentity getCurrentIdentity() {
        return identity;
    }

    private <T> T executeRequest(Request request, Class<T> responseClass) throws ApiAccessException {
        try {
            OkHttpClient client = getHttpClient();
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

    private void handleFailedResponse(Response response) throws Exception {
        if (response.code() == HttpURLConnection.HTTP_NOT_FOUND) {
            throw new EntityNotFoundException();
        }
    }

    private OkHttpClient getHttpClient() throws CustomSSLCertificates.CustomSSLCertificatesException {
        Set<String> certificateStrings = configurationSource.getStringSet("TRUSTED_CERTIFICATES");
        if (certificateStrings == null) certificateStrings = new HashSet<>();

        httpClient.setSslSocketFactory(
                CustomSSLCertificates.createSSLSocketFactoryWithTrustedCertificates(
                        certificateStrings.toArray(new String[certificateStrings.size()])));

        return httpClient;
    }

    private String buildAbsoluteApiUrl(String relativeApiUrl) throws ApiAccessException {
        String tfsUrl = configurationSource.getString("TFS_URL");
        if (StringUtilities.isNullOrEmpty(tfsUrl)) {
            throw new ApiAccessException("TFS Url not configured.", null);
        }

        return tfsUrl + relativeApiUrl;
    }
}
