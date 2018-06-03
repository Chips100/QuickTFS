package quicktfs.app.workItemDetails;

import android.content.Context;

import quicktfs.apiclients.contracts.exceptions.ApiAccessException;
import quicktfs.apiclients.contracts.WorkItemAssignClient;
import quicktfs.app.AsyncApiClientTask;

/**
 * A task for assigning a Work Item to the current user asynchronously.
 */
abstract class AsyncAssignWorkItemToMeTask extends AsyncApiClientTask<AsyncAssignWorkItemToMeTask.AssignWorkItemToMeParams, AsyncAssignWorkItemToMeTask.AssignWorkItemToMeResult> {
    private final WorkItemAssignClient client;

    /**
     * Creates an AsyncAssignWorkItemToMeTask.
     * @param context Context from which the task is started.
     * @param client Client to use for assigning the Work Item.
     */
    AsyncAssignWorkItemToMeTask(Context context, WorkItemAssignClient client) {
        super(context);
        this.client = client;
    }

    @Override
    protected AssignWorkItemToMeResult doApiAction(AssignWorkItemToMeParams assignWorkItemToMeParams) throws ApiAccessException {
        client.assignToMe(assignWorkItemToMeParams.getWorkItemId());
        return new AssignWorkItemToMeResult();
    }

    /**
     * Represents parameters for assigning a Work Item to the current user.
     */
    static class AssignWorkItemToMeParams {
        private final int workItemId;

        /**
         * Creates AssignWorkItemToMeParams.
         * @param workItemId ID of the Work Item that should be assigned to the current user.
         */
        AssignWorkItemToMeParams(int workItemId) {
            this.workItemId = workItemId;
        }

        /**
         * Gets the ID of the Work Item that should be assigned to the current user.
         * @return The ID of the Work Item that should be assigned to the current user.
         */
        int getWorkItemId() { return workItemId; }
    }

    /**
     * Represents the result of the assign operation.
     */
    static class AssignWorkItemToMeResult { }
}