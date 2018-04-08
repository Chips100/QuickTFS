package quicktfs.apiclients.contracts;

/**
 * Client that allows assigning a Work Item to a user.
 */
public interface WorkItemAssignClient {
    /**
     * Assigns the specified Work Item to the current user.
     * @param workItemId ID of the Work Item.
     */
    void assignToMe(int workItemId) throws ApiAccessException;
}