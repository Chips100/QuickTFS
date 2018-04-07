package quicktfs.apiclients.restapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import quicktfs.apiclients.contracts.ApiAccessException;
import quicktfs.apiclients.restapi.SSL.CustomSSLCertificates;

/**
 * Base class for Clients using the TFS HTTP Rest API.
 */
abstract class RestApiClientBase {
    private final Gson gson = new GsonBuilder().create();
    private final String restApiUrl;

    /**
     * Creates a RestApiLoginClient.
     * @param restApiUrl The URL of the TFS HTTP Rest API.
     */
    RestApiClientBase(String restApiUrl) {
        this.restApiUrl = restApiUrl;
    }

    /**
     * Should be overridden to return a component
     * that provides login data to the API calls.
     * @return An instance of RestApiLogin to provide login data to the API calls.
     */
    protected abstract RestApiLogin getLogin();

    /**
     * Makes an HTTP GET Api call.
     * @param apiUrl Relative URL of the REST Api action that should be called.
     * @return The result returned from the Api.
     * @throws ApiAccessException Thrown if any errors occur by accessing the Api.
     */
    <T> T callApiGet(String apiUrl, Class<T> responseClass) throws ApiAccessException {
        return executeRequest(new Request.Builder()
                .url(restApiUrl + apiUrl)
                .get()
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
        getLogin().configureLogin(client);
        client.setSslSocketFactory(CustomSSLCertificates.createSSLSocketFactoryWithTrustedCertificates(CustomSSLCertificates.DataportCertificate));
        return client;
    }
}