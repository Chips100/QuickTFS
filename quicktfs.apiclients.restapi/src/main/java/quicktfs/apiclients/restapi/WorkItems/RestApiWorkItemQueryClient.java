package quicktfs.apiclients.restapi.WorkItems;

import quicktfs.apiclients.contracts.ApiAccessException;
import quicktfs.apiclients.contracts.ConfigurationSource;
import quicktfs.apiclients.contracts.WorkItemDetailsDto;
import quicktfs.apiclients.contracts.WorkItemQueryClient;
import quicktfs.apiclients.restapi.Authentication.AuthenticationState;

/**
 * Client that allows querying Work Items using the HTTP Rest API.
 */
public class RestApiWorkItemQueryClient extends RestApiWorkItemClientBase implements WorkItemQueryClient {
    /**
     * Creates a RestApiLoginClient.
     * @param configurationSource Configuration source for looking up configuration settings.
     * @param authentication Authentication for the TFS HTTP Rest API.
     */
    public RestApiWorkItemQueryClient(ConfigurationSource configurationSource, AuthenticationState authentication) {
        super(configurationSource, authentication);
    }

    /**
     * Queries a single Work Item by its ID.
     * @param id ID of the Work Item.
     * @return The Work Item with the specified ID; or null if it does not exist.
     */
    @Override
    public WorkItemDetailsDto queryById(int id) throws ApiAccessException {
        // Use base functionality for loading the Work Item
        // and simply map the properties to the contracts DTO.
        RestApiWorkItem workItem = getWorkItemById(id);

        return new WorkItemDetailsDto(id,
            workItem.fields.teamProject,
            workItem.fields.type,
            workItem.fields.title,
            workItem.fields.description,
            workItem.fields.assignedTo,
            workItem.fields.activity,
            workItem.fields.iterationPath,
            workItem.fields.state);
    }
}