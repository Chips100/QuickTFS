package quicktfs.app.home;

import java.util.List;

import quicktfs.apiclients.contracts.exceptions.ApiAccessException;
import quicktfs.apiclients.contracts.projects.ProjectClient;
import quicktfs.apiclients.contracts.projects.ProjectDto;
import quicktfs.app.AsyncApiClientTask;

/**
 * A task for loading the TFS Projects asynchronously.
 */
abstract class AsyncLoadProjectsTask extends AsyncApiClientTask<HomeActivity, AsyncLoadProjectsTask.LoadProjectsParams, AsyncLoadProjectsTask.LoadProjectsResult> {
    private final ProjectClient client;

    /**
     * Creates an AsyncLoadProjectsTask.
     * @param context Context from which the task is started.
     * @param client Client to use for loading the TFS Projects.
     */
    AsyncLoadProjectsTask(HomeActivity context, ProjectClient client) {
        super(context);
        this.client = client;
    }

    @Override
    protected LoadProjectsResult doApiAction(LoadProjectsParams params) throws ApiAccessException {
        return new LoadProjectsResult(client.getAllProjects());
    }

    /**
     * Represents parameters for this task.
     */
    static class LoadProjectsParams { }

    /**
     * Represents the result of this task.
     */
    static class LoadProjectsResult {
        private final List<ProjectDto> projects;

        /**
         * Creates a LoadProjectsResult.
         * @param projects Loaded TFS Projects.
         */
        public LoadProjectsResult(List<ProjectDto> projects) {
            this.projects = projects;
        }

        /**
         * Gets the loaded TFS Projects.
         * @return The loaded TFS Projects.
         */
        public List<ProjectDto> getProjects() {
            return this.projects;
        }
    }
}