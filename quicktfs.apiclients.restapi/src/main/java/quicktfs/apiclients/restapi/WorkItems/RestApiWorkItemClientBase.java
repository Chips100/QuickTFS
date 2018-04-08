package quicktfs.apiclients.restapi.WorkItems;

import quicktfs.apiclients.contracts.ApiAccessException;
import quicktfs.apiclients.restapi.Authentication.AuthenticationState;
import quicktfs.apiclients.restapi.RestApiClientBase;

/**
 * Base implementation for RestApiClients that deal with Work Items.
 */
public abstract class RestApiWorkItemClientBase extends RestApiClientBase {
    /**
     * Creates a RestApiWorkItemClientBase.
     * @param restApiUrl The URL of the TFS HTTP Rest API.
     * @param authentication Authentication for the TFS HTTP Rest API.
     */
    public RestApiWorkItemClientBase(String restApiUrl, AuthenticationState authentication) {
        super(restApiUrl, authentication);
    }

    /**
     * Queries a single Work Item by its ID.
     * @param id ID of the Work Item.
     * @return The Work Item with the specified ID; or null if it does not exist.
     */
    protected final RestApiWorkItem getWorkItemById(int id) throws ApiAccessException {
        ensureLoggedIn();
        return callApiGet("_apis/wit/workitems/" + String.valueOf(id), RestApiWorkItem.class);
    }
}