package quicktfs.apiclients.restapi;

import quicktfs.apiclients.contracts.exceptions.ApiAccessException;
import quicktfs.apiclients.restapi.authentication.AuthenticatedIdentity;

/**
 * Mock implementation of the RestClient that allows
 * configuration of its behaviour for unit tests.
 */
public class RestClientMock implements RestClient {
    private AuthenticatedIdentity currentIdentity;
    private Credentials credentials;

    private GetBehaviour getBehaviour;
    private PatchBehaviour patchBehaviour;

    public void configureGetBehaviour(GetBehaviour behaviour) {
        getBehaviour = behaviour;
    }

    public void configurePatchBehaviour(PatchBehaviour behaviour) {
        patchBehaviour = behaviour;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    @Override
    public <T> T get(String apiUrl, Class<T> responseClass) throws ApiAccessException {
        if (getBehaviour == null) throw new ApiAccessException("No GetBehaviour configured.", null);
        return getBehaviour.get(apiUrl, responseClass);
    }

    @Override
    public <TBody, TResponse> TResponse patch(String apiUrl, TBody body, Class<TResponse> responseClass) throws ApiAccessException {
        if (patchBehaviour == null) throw new ApiAccessException("No PatchBehaviour configured.", null);
        return patchBehaviour.patch(apiUrl, body, responseClass);
    }


    @Override
    public void setCredentials(String domain, String username, String password) {
        credentials = new Credentials(domain, username, password);
    }

    @Override
    public void setCurrentIdentity(AuthenticatedIdentity identity) {
        currentIdentity = identity;
    }

    @Override
    public AuthenticatedIdentity getCurrentIdentity() {
        return currentIdentity;
    }

    public static class Credentials {
        private final String domain;
        private final String username;
        private final String password;

        private Credentials(String domain, String username, String password) {
            this.domain = domain;
            this.username = username;
            this.password = password;
        }

        public String getDomain() {
            return this.domain;
        }

        public String getUsername() {
            return this.username;
        }

        public String getPassword() {
            return this.password;
        }
    }

    public interface GetBehaviour {
        <T> T get(String apiUrl, Class<T> responseClass) throws ApiAccessException;
    }

    public interface PatchBehaviour {
        <TBody, TResponse> TResponse patch(String apiUrl, TBody body, Class<TResponse> responseClass)
                throws ApiAccessException;
    }
}
