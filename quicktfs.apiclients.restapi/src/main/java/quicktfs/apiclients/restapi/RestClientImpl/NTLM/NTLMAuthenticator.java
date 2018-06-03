package quicktfs.apiclients.restapi.RestClientImpl.NTLM;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.Proxy;
import java.util.List;

import quicktfs.apiclients.restapi.authentication.AuthenticationFailedException;

/**
 * Authenticator for Windows Integrated Authentication via NTLM.
 * https://github.com/square/okhttp/issues/206
 * Adaptions to limit the number of retries.
 */
public class NTLMAuthenticator implements Authenticator {
    private final NTLMEngineImpl engine = new NTLMEngineImpl();
    private final String domain;
    private final String username;
    private final String password;
    private final String ntlmMsg1;

    // We keep track of the attempted retries to abort after a specified maximum number.
    // Otherwise, the authenticator would retry infinitely with wrong credentials.
    private int retryCount = 0;
    private final int maxRetryCount = 2;

    public NTLMAuthenticator(String username, String password, String domain) {
        this.domain = domain;
        this.username = username;
        this.password = password;
        String localNtlmMsg1 = null;
        try {
            localNtlmMsg1 = engine.generateType1Msg(null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ntlmMsg1 = localNtlmMsg1;
    }

    @Override
    public Request authenticate(Proxy proxy, Response response) throws IOException {
        // Reset Retry-Counter for new requests.
        if (response.priorResponse() == null) { retryCount = 0; }

        // Check if maximum number of retries has been exceeded.
        if (retryCount++ > maxRetryCount) {
            throw new IOException("Error in NTLM authentication.", new AuthenticationFailedException());
        }

        final List<String> WWWAuthenticate = response.headers().values("WWW-Authenticate");
        if (WWWAuthenticate.contains("NTLM")) {
            return response.request().newBuilder().header("Authorization", "NTLM " + ntlmMsg1).build();
        }
        String ntlmMsg3 = null;
        try {
            ntlmMsg3 = engine.generateType3Msg(username, password, domain, "android-device", WWWAuthenticate.get(0).substring(5));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.request().newBuilder().header("Authorization", "NTLM " + ntlmMsg3).build();
    }

    @Override
    public Request authenticateProxy(Proxy proxy, Response response) {
        return null;
    }
}