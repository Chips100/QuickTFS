package quicktfs.apiclients.restapi.projects;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

import quicktfs.apiclients.contracts.exceptions.ApiAccessException;
import quicktfs.apiclients.contracts.projects.ProjectDto;
import quicktfs.apiclients.restapi.RestClientMock;

/**
 * Tests for the RestApiProjectClient.
 */
public class RestApiProjectClientTests {
    /**
     * getProjects should fetch projects from the service
     * and map them to the DTOs specified in the contracts.
     */
    @Test
    public void RestApiProjectClient_getProjects_success() throws Exception {
        RestClientMock restClientMock = new RestClientMock();
        restClientMock.configureGetBehaviour(new RestClientMock.GetBehaviour() {
            @Override
            public <T> T get(String apiUrl, Class<T> responseClass) throws ApiAccessException {
                if (!apiUrl.equals("_apis/projects?api-version=1.0")) {
                    throw new ApiAccessException("Wrong Projects URL");
                }

                if (responseClass != RestApiProjectClient.GetProjectsResponse.class) {
                    throw new ApiAccessException("Wrong expected Response class");
                }

                RestApiProjectClient.GetProjectsResponse response = new RestApiProjectClient.GetProjectsResponse();
                response.value = new RestApiProjectClient.GetProjectsResponseItem[] {
                    createProjectItem("name1", "description1"),
                    createProjectItem("name2", "description2")
                };

                return (T)response;
            }
        });

        RestApiProjectClient sut = new RestApiProjectClient(restClientMock);
        List<ProjectDto> result = sut.getAllProjects();

        assertEquals(2, result.size());
        assertEquals("name1", result.get(0).getName());
        assertEquals("description1", result.get(0).getDescription());
        assertEquals("name2", result.get(1).getName());
        assertEquals("description2", result.get(1).getDescription());
    }

    private RestApiProjectClient.GetProjectsResponseItem createProjectItem(String name, String description) {
        RestApiProjectClient.GetProjectsResponseItem item = new RestApiProjectClient.GetProjectsResponseItem();
        item.name = name;
        item.description = description;
        return item;
    }
}