package quicktfs.apiclients.restapi;

/**
 * Base class for Clients using the TFS HTTP Rest API.
 */
public abstract class RestApiClientBase {
    private final String restApiUrl;

    /**
     * Creates a RestApiLoginClient.
     * @param restApiUrl The URL of the TFS HTTP Rest API.
     */
    protected RestApiClientBase(String restApiUrl) {
        this.restApiUrl = restApiUrl;
    }
}