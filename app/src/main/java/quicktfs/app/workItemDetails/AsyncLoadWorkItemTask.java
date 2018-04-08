package quicktfs.app.workItemDetails;

import android.content.Context;

import quicktfs.apiclients.contracts.ApiAccessException;
import quicktfs.apiclients.contracts.WorkItemDetailsDto;
import quicktfs.apiclients.contracts.WorkItemQueryClient;
import quicktfs.app.AsyncApiClientTask;

/**
 * A task for loading the details of a Work Item asynchronously.
 */
abstract class AsyncLoadWorkItemTask extends AsyncApiClientTask<AsyncLoadWorkItemTask.LoadWorkItemParams, AsyncLoadWorkItemTask.LoadWorkItemResult> {
    private final WorkItemQueryClient client;

    /**
     * Creates an AsyncLoadWorkItemTask.
     * @param context Context from which the task is started.
     * @param client Client to use for loading the Work Item.
     */
    AsyncLoadWorkItemTask(Context context, WorkItemQueryClient client) {
        super(context);
        this.client = client;
    }

    @Override
    protected LoadWorkItemResult doApiAction(LoadWorkItemParams loadWorkItemParams) throws ApiAccessException {
        WorkItemDetailsDto workItem = client.queryById(loadWorkItemParams.getWorkItemId());
        return new LoadWorkItemResult(workItem);
    }

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