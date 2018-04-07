package quicktfs.app;

import java.util.HashMap;
import java.util.Map;

import quicktfs.apiclients.contracts.LoginClient;
import quicktfs.apiclients.contracts.WorkItemAssignClient;
import quicktfs.apiclients.contracts.WorkItemQueryClient;
import quicktfs.apiclients.restapi.RestApiLogin;
import quicktfs.apiclients.restapi.RestApiLoginClient;
import quicktfs.apiclients.restapi.RestApiWorkItemAssignClient;
import quicktfs.apiclients.restapi.RestApiWorkItemQueryClient;

/**
 * Stub for an IOC container as a temporary solution
 * until real DI is introduced to the project.
 */
public class IocContainerStub {
    private static final Map<String, IocContainerStub> instances = new HashMap<>();

    // Singletons.
    private RestApiLogin loginClient;
    private WorkItemQueryClient workItemQueryClient;
    private WorkItemAssignClient workItemAssignClient;

    private IocContainerStub(String tfsUrl) {
        loginClient = new RestApiLoginClient(tfsUrl);
        workItemQueryClient = new RestApiWorkItemQueryClient(tfsUrl, loginClient);
        workItemAssignClient = new RestApiWorkItemAssignClient(tfsUrl, loginClient);
    }

    private static IocContainerStub getInstance(String tfsUrl) {
        if (!instances.containsKey(tfsUrl)) {
            instances.put(tfsUrl, new IocContainerStub(tfsUrl));
        }

        return instances.get(tfsUrl);
    }

    /**
     * Gets a reference to an IocContainerStub for the default TFS.
     * @return A reference to an IocContainerStub for the default TFS.
     */
    public static IocContainerStub getDefaultInstance() {
        return IocContainerStub.getInstance("https://tfs2.dataport.de/tfs_2/CCSE/_apis/");
    }

    /**
     * Gets an instance of the specified type.
     * @param clazz Class info about the type to get.
     * @param <T> Type of the instance to get.
     * @return An instance of the specified type.
     */
    public<T> T getInstance(Class<T> clazz) {
        if (clazz == LoginClient.class || clazz == RestApiLogin.class) {
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