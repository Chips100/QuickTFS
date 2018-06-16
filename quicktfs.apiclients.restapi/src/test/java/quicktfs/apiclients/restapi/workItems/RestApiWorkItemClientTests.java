package quicktfs.apiclients.restapi.workItems;

import org.junit.Test;

import quicktfs.apiclients.contracts.WorkItemAssignClient;
import quicktfs.apiclients.contracts.WorkItemDetailsDto;
import quicktfs.apiclients.contracts.WorkItemQueryClient;
import quicktfs.apiclients.contracts.exceptions.ApiAccessException;
import quicktfs.apiclients.restapi.RestClientMock;
import quicktfs.apiclients.restapi.WorkItems.RestApiWorkItem;
import quicktfs.apiclients.restapi.WorkItems.RestApiWorkItemClient;
import quicktfs.apiclients.restapi.authentication.AuthenticatedIdentity;

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

    /**
     * assignToMe should first query the Work Item again to decide
     * if the "AssignedTo" field should be replaced or added. (Test case for replace).
     */
    @Test
    public void RestApiWorkItemClient_assignToMe_success_replace() throws Exception {
        RestClientMock restClientMock = new RestClientMock();

        // Work Item is already assigned to someone.
        RestApiWorkItem workItem = new RestApiWorkItem();
        workItem.id = 42;
        workItem.fields = new RestApiWorkItem.RestApiWorkItemFields();
        workItem.fields.assignedTo = "someoneelse";
        restClientMock.configureGetBehaviour(new GetWorkItemByIdGetBehaviour(workItem));

        // Set current identity to which the task should be assigned.
        restClientMock.setCurrentIdentity(new AuthenticatedIdentity(
                "domain", "accountName",
                "displayName", null));

        // Watch for patch request with the update.
        restClientMock.configurePatchBehaviour(new RestClientMock.PatchBehaviour() {
            @Override
            public <TBody> boolean condition(String apiUrl, TBody tBody, Class responseClass) {
                return apiUrl.equals("_apis/wit/workitems/42?api-version=1.0");
            }

            @Override
            public <TBody, TResponse> TResponse patch(String apiUrl, TBody body, Class<TResponse> tResponseClass) throws ApiAccessException {
                assertTrue(body instanceof RestApiWorkItemClient.WorkItemUpdateOperation[]);

                RestApiWorkItemClient.WorkItemUpdateOperation updateOperation =
                        ((RestApiWorkItemClient.WorkItemUpdateOperation[])body)[0];

                assertEquals("replace", updateOperation.op);
                assertEquals("/fields/System.AssignedTo", updateOperation.path);
                assertEquals("displayName <domain\\accountName>", updateOperation.value);
                return null;
            }
        });

        WorkItemAssignClient sut = new RestApiWorkItemClient(restClientMock);
        sut.assignToMe(42);
    }

    /**
     * assignToMe should first query the Work Item again to decide
     * if the "AssignedTo" field should be replaced or added. (Test case for add).
     */
    @Test
    public void RestApiWorkItemClient_assignToMe_success_add() throws Exception {
        RestClientMock restClientMock = new RestClientMock();

        // Work Item is already assigned to someone.
        RestApiWorkItem workItem = new RestApiWorkItem();
        workItem.id = 42;
        workItem.fields = new RestApiWorkItem.RestApiWorkItemFields();
        workItem.fields.assignedTo = null;
        restClientMock.configureGetBehaviour(new GetWorkItemByIdGetBehaviour(workItem));

        // Set current identity to which the task should be assigned.
        restClientMock.setCurrentIdentity(new AuthenticatedIdentity(
                "domain", "accountName",
                "displayName", null));

        // Watch for patch request with the update.
        restClientMock.configurePatchBehaviour(new RestClientMock.PatchBehaviour() {
            @Override
            public <TBody> boolean condition(String apiUrl, TBody tBody, Class responseClass) {
                return apiUrl.equals("_apis/wit/workitems/42?api-version=1.0");
            }

            @Override
            public <TBody, TResponse> TResponse patch(String apiUrl, TBody body, Class<TResponse> tResponseClass) throws ApiAccessException {
                assertTrue(body instanceof RestApiWorkItemClient.WorkItemUpdateOperation[]);

                RestApiWorkItemClient.WorkItemUpdateOperation updateOperation =
                        ((RestApiWorkItemClient.WorkItemUpdateOperation[])body)[0];

                assertEquals("add", updateOperation.op);
                assertEquals("/fields/System.AssignedTo", updateOperation.path);
                assertEquals("displayName <domain\\accountName>", updateOperation.value);
                return null;
            }
        });

        WorkItemAssignClient sut = new RestApiWorkItemClient(restClientMock);
        sut.assignToMe(42);
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
