package quicktfs.apiclients.contracts;

/**
 * Client that allows logging in to a TFS Api.
 */
public interface LoginClient {
    /**
     * Tries logging in with the specified credentials.
     * @param domain Domain of the account to use for logging in.
     * @param username UserName of the account to use for logging in.
     * @param password Password of the account to use for logging in.
     * @return True, if the login was successful; otherwise false.
     */
    boolean tryLogin(String domain, String username, String password) throws ApiAccessException;
}