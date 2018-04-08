package quicktfs.apiclients.restapi.WorkItems;

import quicktfs.apiclients.contracts.ApiAccessException;
import quicktfs.apiclients.contracts.ConfigurationSource;
import quicktfs.apiclients.contracts.WorkItemAssignClient;
import quicktfs.apiclients.restapi.Authentication.AuthenticatedIdentity;
import quicktfs.apiclients.restapi.Authentication.AuthenticationState;

/**
 * Client that allows assigning a Work Item to a user using the HTTP Rest API.
 */
public class RestApiWorkItemAssignClient extends RestApiWorkItemClientBase implements WorkItemAssignClient {
    /**
     * Creates a RestApiWorkItemAssignClient.
     * @param configurationSource Configuration source for looking up configuration settings.
     * @param authentication Authentication for the TFS HTTP Rest API.
     */
    public RestApiWorkItemAssignClient(ConfigurationSource configurationSource, AuthenticationState authentication) {
        super(configurationSource, authentication);
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
        AuthenticatedIdentity identity = getAuthentication().getIdentity();

        // Build the update operation.
        WorkItemUpdateOperation update = new WorkItemUpdateOperation();
        update.op = workItem.fields.assignedTo == null ? "add" : "replace";
        update.path = "/fields/System.AssignedTo";
        update.value = identity.getDisplayName() + " <" + identity.getDomain() + "\\" + identity.getAccountName() + ">";

        // Send the update operation.
        callApiPatch("_apis/wit/workitems/" + String.valueOf(workItemId) + "?api-version=1.0", new WorkItemUpdateOperation[] { update }, WorkItemUpdateResponse.class);
    }

    public static class WorkItemUpdateOperation {
        public String op;
        public String path;
        public Object value;
    }

    public static class WorkItemUpdateResponse { }
}