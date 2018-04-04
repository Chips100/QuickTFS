package quicktfs.apiclients.restapi;

import quicktfs.apiclients.contracts.WorkItemDetailsDto;
import quicktfs.apiclients.contracts.WorkItemQueryClient;

/**
 * Client that allows querying Work Items using the HTTP Rest API.
 */
public class RestApiWorkItemQueryClient extends RestApiClientBase implements WorkItemQueryClient {
    /**
     * Creates a RestApiLoginClient.
     * @param restApiUrl The URL of the TFS HTTP Rest API.
     */
    public RestApiWorkItemQueryClient(String restApiUrl) {
        super(restApiUrl);
    }

    /**
     * Queries a single Work Item by its ID.
     * @param id ID of the Work Item.
     * @return The Work Item with the specified ID; or null if it does not exist.
     */
    @Override
    public WorkItemDetailsDto queryById(int id) {
        return new WorkItemDetailsDto(id, "Titel " + id, "Description " + id);
    }
}