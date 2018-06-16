package quicktfs.apiclients.contracts.projects;

import java.util.List;

import quicktfs.apiclients.contracts.exceptions.ApiAccessException;

/**
 * Client that allows access to the TFS Projects.
 */
public interface ProjectClient {
    /**
     * Gets all TFS Projects of the current user.
     * @return A list of all TFS Projects of the current user.
     */
    List<ProjectDto> getAllProjects() throws ApiAccessException;
}