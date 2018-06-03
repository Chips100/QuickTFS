package quicktfs.apiclients.contracts;

import quicktfs.apiclients.contracts.exceptions.ApiAccessException;

/**
 * Client that allows querying Work Items.
 */
public interface WorkItemQueryClient {
    /**
     * Queries a single Work Item by its ID.
     * @param id ID of the Work Item.
     * @return The Work Item with the specified ID; or null if it does not exist.
     */
    WorkItemDetailsDto queryById(int id) throws ApiAccessException;
}