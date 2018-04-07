package quicktfs.apiclients.restapi;

import quicktfs.apiclients.contracts.WorkItemAssignClient;

/**
 * Client that allows assigning a Work Item to a user using the HTTP Rest API.
 */
public class RestApiWorkItemAssignClient extends RestApiClientBase implements WorkItemAssignClient {
    private final RestApiLogin login;

    /**
     * Creates a RestApiWorkItemAssignClient.
     * @param restApiUrl The URL of the TFS HTTP Rest API.
     * @param login Login provider for the TFS HTTP Rest API.
     */
    public RestApiWorkItemAssignClient(String restApiUrl, RestApiLogin login) {
        super(restApiUrl);
        this.login = login;
    }

    /**
     * Assigns the specified Work Item to the specified user.
     * @param workItemId ID of the Work Item.
     */
    @Override
    public void assignToMe(int workItemId) {
        try {
            Thread.sleep(3000);
        } catch(Exception exception) {}
    }
}