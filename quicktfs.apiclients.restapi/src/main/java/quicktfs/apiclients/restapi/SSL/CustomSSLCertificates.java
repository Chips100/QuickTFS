package quicktfs.apiclients.restapi.SSL;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Allows configuration of custom SSL certificates for HTTP requests.
 */
public class CustomSSLCertificates {

    /**
     * Thrown if any errors occur in the configuration of SSL certificates.
     */
    public static class CustomSSLCertificatesException extends Exception {
        private CustomSSLCertificatesException(Exception innerException) {
            super("Error configuring SSL certificates.", innerException);
        }
    }

    /**
     * Creates an SSLSocketFactory that trusts the specified certificates.
     * The certificate strings should be in Base64 encoded X.509 format.
     * @param certificateStrings String representations of trusted certificates in Base64 encoded X.509 format.
     * @return An SSLSocketFactory that trusts the specified certificates.
     * @throws CustomSSLCertificatesException Thrown if any errors occur in the configuration of SSL certificates.
     */
    public static SSLSocketFactory createSSLSocketFactoryWithTrustedCertificates(String... certificateStrings)
            throws CustomSSLCertificatesException {
        try {
            return createSSLSocketFactory(certificateStrings);
        }
        catch(Exception exception) {
            throw new CustomSSLCertificatesException(exception);
        }
    }

    private static SSLSocketFactory createSSLSocketFactory(String... certificateStrings)
            throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, KeyManagementException {
        // https://developer.android.com/training/articles/security-ssl.html
        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);

        // Register all specified certificates.
        for (int i = 0; i < certificateStrings.length; i++) {
            Certificate ca = createCertificateFromString(certificateStrings[i]);

            // Just use an incrementing alias, has no further meaning:
            // https://stackoverflow.com/questions/27070972/how-to-load-multiple-ssl-certificates-in-java-keystore
            keyStore.setCertificateEntry("ca" + String.valueOf(i), ca);
        }

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Create an SSLContext that uses our TrustManager
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);

        // Tell the URLConnection to use a SocketFactory from our SSLContext
        return context.getSocketFactory();
    }

    private static Certificate createCertificateFromString(String certificateString)
            throws CertificateException, IOException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream caInput = new ByteArrayInputStream(certificateString.getBytes("UTF-8"));

        try {
            return cf.generateCertificate(caInput);
        } finally {
            caInput.close();
        }
    }
}