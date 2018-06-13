package quicktfs.apiclients.restapi;

import java.util.ArrayList;
import java.util.List;

import quicktfs.apiclients.contracts.exceptions.ApiAccessException;
import quicktfs.apiclients.restapi.authentication.AuthenticatedIdentity;

/**
 * Mock implementation of the RestClient that allows
 * configuration of its behaviour for unit tests.
 */
public class RestClientMock implements RestClient {
    private Credentials credentials;
    // "Shortcut - logged in by default.
    private AuthenticatedIdentity currentIdentity = new AuthenticatedIdentity(
        "Domain", "AccountName", "DisplayName", "test@test.de"
    );

    private final List<GetBehaviour> getBehaviours = new ArrayList<>();
    private final List<PatchBehaviour> patchBehaviours = new ArrayList<>();

    public void configureGetBehaviour(GetBehaviour behaviour) {
        getBehaviours.add(behaviour);
    }

    public void configurePatchBehaviour(PatchBehaviour behaviour) {
        patchBehaviours.add(behaviour);
    }

    public Credentials getCredentials() {
        return credentials;
    }

    @Override
    public <T> T get(String apiUrl, Class<T> responseClass) throws ApiAccessException {
        for(GetBehaviour behaviour : getBehaviours) {
            if (behaviour.condition(apiUrl, responseClass)) {
                return behaviour.get(apiUrl, responseClass);
            }
        }

        throw new ApiAccessException("No suitable GetBehaviour configured.", null);
    }

    @Override
    public <TBody, TResponse> TResponse patch(String apiUrl, TBody body, Class<TResponse> responseClass) throws ApiAccessException {
        for(PatchBehaviour behaviour : patchBehaviours) {
            if (behaviour.condition(apiUrl, body, responseClass)) {
                return behaviour.patch(apiUrl, body, responseClass);
            }
        }

        throw new ApiAccessException("No suitable PatchBehaviour configured.", null);
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

    public static abstract class GetBehaviour {
        public abstract <T> T get(String apiUrl, Class<T> responseClass) throws ApiAccessException;

        public boolean condition(String apiUrl, Class responseClass) { return true; }
    }

    public static abstract class PatchBehaviour {
        public abstract <TBody, TResponse> TResponse patch(String apiUrl, TBody body, Class<TResponse> responseClass)
                throws ApiAccessException;

        public <TBody>  boolean condition(String apiUrl, TBody body, Class responseClass) { return true; }
    }
}
