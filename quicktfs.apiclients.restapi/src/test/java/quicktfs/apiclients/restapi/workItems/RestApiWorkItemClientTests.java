package quicktfs.apiclients.restapi.workItems;

import org.junit.Test;

import quicktfs.apiclients.contracts.WorkItemDetailsDto;
import quicktfs.apiclients.contracts.WorkItemQueryClient;
import quicktfs.apiclients.contracts.exceptions.ApiAccessException;
import quicktfs.apiclients.restapi.RestClientMock;
import quicktfs.apiclients.restapi.WorkItems.RestApiWorkItem;
import quicktfs.apiclients.restapi.WorkItems.RestApiWorkItemClient;

import static org.junit.Assert.*;

/**
 * Tests for the RestApiWorkItemClient.
 */
public class RestApiWorkItemClientTests {
    /**
     * queryById should load the WorkItem with the specified
     * ID from the RestClient and map it to a DTO instance.
     */
    @Test
    public void RestApiWorkItemClient_queryById_success() throws Exception {
        // Mock RestClient that provides a Work Item when queried by id.
        RestApiWorkItem workItem = new RestApiWorkItem();
        workItem.id = 42;
        workItem.fields = new RestApiWorkItem.RestApiWorkItemFields();
        workItem.fields.teamProject = "teamProject";
        workItem.fields.type = "type";
        workItem.fields.title = "title";
        workItem.fields.description = "description";
        workItem.fields.assignedTo = "assignedTo";
        workItem.fields.activity = "activity";
        workItem.fields.iterationPath = "iterationPath";
        workItem.fields.state = "state";

        RestClientMock restClientMock = new RestClientMock();
        restClientMock.configureGetBehaviour(new GetWorkItemByIdGetBehaviour(workItem));

        WorkItemQueryClient sut = new RestApiWorkItemClient(restClientMock);
        WorkItemDetailsDto result = sut.queryById(42);

        assertNotNull(result);
        assertEquals(42, result.getId());
        assertEquals("teamProject", result.getTeamProject());
        assertEquals("type", result.getType());
        assertEquals("title", result.getTitle());
        assertEquals("description", result.getDescription());
        assertEquals("assignedTo", result.getAssignedTo());
        assertEquals("activity", result.getActivity());
        assertEquals("iterationPath", result.getIterationPath());
        assertEquals("state", result.getState());
    }

    private static class GetWorkItemByIdGetBehaviour extends RestClientMock.GetBehaviour {
        private final RestApiWorkItem workItem;

        public GetWorkItemByIdGetBehaviour(RestApiWorkItem workItem) {
            this.workItem = workItem;
        }

        @Override
        public <T> T get(String apiUrl, Class<T> responseClass) throws ApiAccessException {
            return (T)workItem;
        }

        @Override
        public boolean condition(String apiUrl, Class responseClass) {
            return apiUrl.equals("_apis/wit/workitems/" + String.valueOf(workItem.id))
                    && responseClass == RestApiWorkItem.class;
        }
    }
}
