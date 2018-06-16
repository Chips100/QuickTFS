package quicktfs.app;

import quicktfs.apiclients.contracts.ConfigurationSource;
import quicktfs.apiclients.contracts.LoginClient;
import quicktfs.apiclients.contracts.WorkItemAssignClient;
import quicktfs.apiclients.contracts.WorkItemQueryClient;
import quicktfs.apiclients.contracts.projects.ProjectClient;
import quicktfs.apiclients.restapi.RestClient;
import quicktfs.apiclients.restapi.RestApiLoginClient;
import quicktfs.apiclients.restapi.RestClientImpl.RestClientImpl;
import quicktfs.apiclients.restapi.WorkItems.RestApiWorkItemClient;
import quicktfs.apiclients.restapi.projects.RestApiProjectClient;

/**
 * Stub for an IOC container as a temporary solution
 * until real DI is introduced to the project.
 */
public class IocContainerStub {
    private static boolean isInitialized = false;

    // Singletons.
    private static ConfigurationSource configurationSource;
    private static LoginClient loginClient;
    private static WorkItemQueryClient workItemQueryClient;
    private static WorkItemAssignClient workItemAssignClient;
    private static ProjectClient projectClient;

    private synchronized static void init() {
        if (isInitialized) return;

        configurationSource = AppConfigurationSource.getInstance();
        RestClient restClient = new RestClientImpl(configurationSource);
        RestApiWorkItemClient workItemClient = new RestApiWorkItemClient(restClient);

        loginClient = new RestApiLoginClient(restClient);
        projectClient = new RestApiProjectClient(restClient);
        workItemQueryClient = workItemClient;
        workItemAssignClient = workItemClient;
        isInitialized = true;
    }

    /**
     * Gets an instance of the specified type.
     * @param clazz Class info about the type to get.
     * @param <T> Type of the instance to get.
     * @return An instance of the specified type.
     */
    @SuppressWarnings("unchecked") // Casts are checked for safety.
    public static<T> T getInstance(Class<T> clazz) {
        if (!isInitialized) init();

        if (clazz == LoginClient.class) {
            return (T)loginClient;
        }
        if (clazz == WorkItemQueryClient.class) {
            return (T)workItemQueryClient;
        }
        if (clazz == WorkItemAssignClient.class) {
            return (T)workItemAssignClient;
        }
        if (clazz == ProjectClient.class) {
            return (T)projectClient;
        }

        throw new IllegalArgumentException("Type not registered in IocContainerStub: " + clazz.getName());
    }
}