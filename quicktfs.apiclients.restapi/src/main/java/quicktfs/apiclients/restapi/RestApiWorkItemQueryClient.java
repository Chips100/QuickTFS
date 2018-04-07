package quicktfs.apiclients.restapi;

import quicktfs.apiclients.contracts.WorkItemDetailsDto;
import quicktfs.apiclients.contracts.WorkItemQueryClient;

/**
 * Client that allows querying Work Items using the HTTP Rest API.
 */
public class RestApiWorkItemQueryClient extends RestApiClientBase implements WorkItemQueryClient {
    private final RestApiLogin login;

    /**
     * Creates a RestApiLoginClient.
     * @param restApiUrl The URL of the TFS HTTP Rest API.
     * @param login Login provider for the TFS HTTP Rest API.
     */
    public RestApiWorkItemQueryClient(String restApiUrl, RestApiLogin login) {
        super(restApiUrl);
        this.login = login;
    }

    /**
     * Queries a single Work Item by its ID.
     * @param id ID of the Work Item.
     * @return The Work Item with the specified ID; or null if it does not exist.
     */
    @Override
    public WorkItemDetailsDto queryById(int id) {
        try {
            Thread.sleep(3000);
        } catch(Exception exception) {}

        return new WorkItemDetailsDto(id, "Titel " + id, "Description " + id);
    }

    /**
     * Should be overridden to return a component
     * that provides login data to the API calls.
     * @return An instance of RestApiLogin to provide login data to the API calls.
     */
    @Override
    protected RestApiLogin getLogin() { return login; }
}