package quicktfs.app;

import quicktfs.apiclients.contracts.ConfigurationSource;
import quicktfs.apiclients.contracts.LoginClient;
import quicktfs.apiclients.contracts.WorkItemAssignClient;
import quicktfs.apiclients.contracts.WorkItemQueryClient;
import quicktfs.apiclients.restapi.Authentication.AuthenticationState;
import quicktfs.apiclients.restapi.Authentication.RestApiAuthenticationState;
import quicktfs.apiclients.restapi.RestApiLoginClient;
import quicktfs.apiclients.restapi.WorkItems.RestApiWorkItemAssignClient;
import quicktfs.apiclients.restapi.WorkItems.RestApiWorkItemQueryClient;

/**
 * Stub for an IOC container as a temporary solution
 * until real DI is introduced to the project.
 */
public class IocContainerStub {
    private static boolean isInitialized = false;

    // Singletons.
    private static ConfigurationSource configurationSource;
    private static AuthenticationState authenticationState;
    private static LoginClient loginClient;
    private static WorkItemQueryClient workItemQueryClient;
    private static WorkItemAssignClient workItemAssignClient;

    private synchronized static void init() {
        if (isInitialized) return;

        configurationSource = AppConfigurationSource.getInstance();
        authenticationState = new RestApiAuthenticationState();
        loginClient = new RestApiLoginClient(configurationSource, authenticationState);
        workItemQueryClient = new RestApiWorkItemQueryClient(configurationSource, authenticationState);
        workItemAssignClient = new RestApiWorkItemAssignClient(configurationSource, authenticationState);
        isInitialized = true;
    }

    /**
     * Gets an instance of the specified type.
     * @param clazz Class info about the type to get.
     * @param <T> Type of the instance to get.
     * @return An instance of the specified type.
     */
    public static<T> T getInstance(Class<T> clazz) {
        if (!isInitialized) init();

        if (clazz == AuthenticationState.class) {
            return (T)authenticationState;
        }
        if (clazz == LoginClient.class) {
            return (T)loginClient;
        }
        if (clazz == WorkItemQueryClient.class) {
            return (T)workItemQueryClient;
        }
        if (clazz == WorkItemAssignClient.class) {
            return (T)workItemAssignClient;
        }

        throw new IllegalArgumentException("Type not registered in IocContainerStub: " + clazz.getName());
    }
}