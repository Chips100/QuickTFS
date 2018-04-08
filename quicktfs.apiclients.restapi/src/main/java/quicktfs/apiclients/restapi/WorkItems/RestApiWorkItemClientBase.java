package quicktfs.apiclients.restapi.WorkItems;

import quicktfs.apiclients.contracts.ApiAccessException;
import quicktfs.apiclients.contracts.ConfigurationSource;
import quicktfs.apiclients.restapi.Authentication.AuthenticationState;
import quicktfs.apiclients.restapi.RestApiClientBase;

/**
 * Base implementation for RestApiClients that deal with Work Items.
 */
public abstract class RestApiWorkItemClientBase extends RestApiClientBase {
    /**
     * Creates a RestApiWorkItemClientBase.
     * @param configurationSource Configuration source for looking up configuration settings.
     * @param authentication Authentication for the TFS HTTP Rest API.
     */
    public RestApiWorkItemClientBase(ConfigurationSource configurationSource, AuthenticationState authentication) {
        super(configurationSource, authentication);
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