package quicktfs.apiclients.restapi;

import com.squareup.okhttp.OkHttpClient;

/**
 * Provides login data for accessing the TFS Rest API.
 */
public interface RestApiLogin {
    /**
     * Configures the login data for the specified client.
     * @param client Client that will be used for accessing the TFS Rest API.
     */
    void configureLogin(OkHttpClient client);
}