package quicktfs.app.workItemDetails;

import android.os.AsyncTask;

import quicktfs.apiclients.contracts.WorkItemAssignClient;

/**
 * A task for assigning a Work Item to the current user asynchronously.
 */
abstract class AsyncAssignWorkItemToMeTask extends AsyncTask<AsyncAssignWorkItemToMeTask.AssignWorkItemToMeParams, Void, AsyncAssignWorkItemToMeTask.AssignWorkItemToMeResult> {
    private final WorkItemAssignClient client;

    /**
     * Creates an AsyncAssignWorkItemToMeTask.
     * @param client Client to use for assigning the Work Item.
     */
    AsyncAssignWorkItemToMeTask(WorkItemAssignClient client) {
        this.client = client;
    }

    @Override
    protected AssignWorkItemToMeResult doInBackground(AssignWorkItemToMeParams... assignWorkItemToMeParams) {
        if (assignWorkItemToMeParams == null || assignWorkItemToMeParams.length != 1) {
            throw new IllegalArgumentException("assignWorkItemToMeParams");
        }

        AssignWorkItemToMeParams params = assignWorkItemToMeParams[0];
        client.assignToMe(params.getWorkItemId());
        return new AssignWorkItemToMeResult();
    }

    /**
     * Should be overridden to define the handling of the completed operation.
     * @param result Result of the assign operation.
     */
    @Override
    protected abstract void onPostExecute(AssignWorkItemToMeResult result);

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