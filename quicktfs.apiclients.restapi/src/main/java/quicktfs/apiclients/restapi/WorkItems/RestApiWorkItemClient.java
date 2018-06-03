package quicktfs.apiclients.restapi.WorkItems;

import quicktfs.apiclients.contracts.WorkItemAssignClient;
import quicktfs.apiclients.contracts.WorkItemDetailsDto;
import quicktfs.apiclients.contracts.WorkItemQueryClient;
import quicktfs.apiclients.contracts.exceptions.ApiAccessException;
import quicktfs.apiclients.contracts.exceptions.NotAuthenticatedException;
import quicktfs.apiclients.restapi.RestClient;
import quicktfs.apiclients.restapi.authentication.AuthenticatedIdentity;

/**
 * Client for working with TFS Work Items.
 */
public class RestApiWorkItemClient implements WorkItemAssignClient, WorkItemQueryClient {
    private final RestClient client;

    /**
     * Creates a RestApiWorkItemClient.
     * @param client Client used for communication with the REST interface.
     */
    public RestApiWorkItemClient(RestClient client) {
        this.client = client;
    }

    /**
     * Queries a single Work Item by its ID.
     * @param id ID of the Work Item.
     * @return The Work Item with the specified ID; or null if it does not exist.
     */
    @Override
    public WorkItemDetailsDto queryById(int id) throws ApiAccessException {
        // Use core functionality for loading the Work Item
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

    /**
     * Assigns the specified Work Item to the specified user.
     * @param workItemId ID of the Work Item.
     */
    @Override
    public void assignToMe(int workItemId) throws ApiAccessException {
        ensureLoggedIn();

        // Load the work item first to check if it is already assigned.
        // Depending on that, we need to use a "replace" or an "add" operation for the update.
        RestApiWorkItem workItem = getWorkItemById(workItemId);
        AuthenticatedIdentity identity = this.client.getCurrentIdentity();

        // Build the update operation.
        WorkItemUpdateOperation update = new WorkItemUpdateOperation();
        update.op = workItem.fields.assignedTo == null ? "add" : "replace";
        update.path = "/fields/System.AssignedTo";
        update.value = identity.getDisplayName() + " <" + identity.getDomain() + "\\" + identity.getAccountName() + ">";

        // Send the update operation.
        this.client.patch("_apis/wit/workitems/" + String.valueOf(workItemId) + "?api-version=1.0", new WorkItemUpdateOperation[] { update }, WorkItemUpdateResponse.class);
    }

    public static class WorkItemUpdateOperation {
        public String op;
        public String path;
        public Object value;
    }

    public static class WorkItemUpdateResponse { }


    /**
     * Queries a single Work Item by its ID.
     * @param id ID of the Work Item.
     * @return The Work Item with the specified ID; or null if it does not exist.
     */
    protected final RestApiWorkItem getWorkItemById(int id) throws ApiAccessException {
        ensureLoggedIn();
        return this.client.get("_apis/wit/workitems/" + String.valueOf(id), RestApiWorkItem.class);
    }

    private void ensureLoggedIn() throws ApiAccessException {
        if (this.client.getCurrentIdentity() == null) {
            throw new ApiAccessException(new NotAuthenticatedException());
        }
    }
}