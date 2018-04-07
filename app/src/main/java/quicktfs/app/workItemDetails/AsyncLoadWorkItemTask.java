package quicktfs.app.workItemDetails;

import android.os.AsyncTask;

import quicktfs.apiclients.contracts.WorkItemDetailsDto;
import quicktfs.apiclients.contracts.WorkItemQueryClient;

/**
 * A task for loading the details of a Work Item asynchronously.
 */
abstract class AsyncLoadWorkItemTask extends AsyncTask<AsyncLoadWorkItemTask.LoadWorkItemParams, Void, AsyncLoadWorkItemTask.LoadWorkItemResult> {
    private final WorkItemQueryClient client;

    /**
     * Creates an AsyncLoadWorkItemTask.
     * @param client Client to use for loading the Work Item.
     */
    AsyncLoadWorkItemTask(WorkItemQueryClient client) {
        this.client = client;
    }

    @Override
    protected LoadWorkItemResult doInBackground(LoadWorkItemParams... loadWorkItemParams) {
        if (loadWorkItemParams == null || loadWorkItemParams.length != 1) {
            throw new IllegalArgumentException("loadWorkItemParams");
        }

        LoadWorkItemParams params = loadWorkItemParams[0];
        WorkItemDetailsDto workItem = client.queryById(params.getWorkItemId());
        return new LoadWorkItemResult(workItem);
    }

    /**
     * Should be overridden to define the handling of the completed operation.
     * @param result Result of the assign operation.
     */
    @Override
    protected abstract void onPostExecute(LoadWorkItemResult result);

    /**
     * Represents parameters for loading the details of a Work Item.
     */
    static class LoadWorkItemParams {
        private final int workItemId;

        /**
         * Creates LoadWorkItemParams.
         * @param workItemId ID of the Work Item that should be loaded.
         */
        LoadWorkItemParams(int workItemId) {
            this.workItemId = workItemId;
        }

        /**
         * Gets the ID of the Work Item that should be loaded.
         * @return The ID of the Work Item that should be loaded.
         */
        int getWorkItemId() { return workItemId; }
    }

    /**
     * Represents the result of the loading operation.
     */
    static class LoadWorkItemResult {
        private final WorkItemDetailsDto workItem;

        /**
         * Creates a LoadWorkItemResult.
         * @param workItem Details of the loaded Work Item.
         */
        LoadWorkItemResult(WorkItemDetailsDto workItem) {
            this.workItem = workItem;
        }

        /**
         * Gets the details of the loaded Work Item.
         * @return The details of the loaded Work Item.
         */
        WorkItemDetailsDto getWorkItem() { return this.workItem; }
    }
}