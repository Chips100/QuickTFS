package quicktfs.apiclients.restapi.projects;

import java.util.ArrayList;
import java.util.List;

import quicktfs.apiclients.contracts.exceptions.ApiAccessException;
import quicktfs.apiclients.contracts.projects.ProjectClient;
import quicktfs.apiclients.contracts.projects.ProjectDto;
import quicktfs.apiclients.restapi.RestClient;

/**
 * Client that allows access to the TFS Projects.
 */
public class RestApiProjectClient implements ProjectClient {
    private final RestClient client;

    /**
     * Creates a RestApiProjectClient.
     * @param client Client used for communication with the REST interface.
     */
    public RestApiProjectClient(RestClient client) {
        this.client = client;
    }

    /**
     * Gets all TFS Projects of the current user.
     * @return A list of all TFS Projects of the current user.
     */
    @Override
    public List<ProjectDto> getAllProjects() throws ApiAccessException {
        // Fetch projects from service.
        GetProjectsResponse response = client.get("_apis/projects?api-version=1.0", GetProjectsResponse.class);

        // Map to DTOs.
        List<ProjectDto> results = new ArrayList<>();
        for (GetProjectsResponseItem item: response.value) {
            results.add(new ProjectDto(item.name, item.description));
        }

        return results;
    }

    public static class GetProjectsResponse {
        public GetProjectsResponseItem[] value;
    }

    public static class GetProjectsResponseItem {
        public String name;
        public String description;
    }
}
