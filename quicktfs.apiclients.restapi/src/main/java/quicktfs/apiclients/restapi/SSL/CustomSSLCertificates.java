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

    public final static String DataportCertificate
            = "-----BEGIN CERTIFICATE-----\n" +
            "MIIFHDCCBASgAwIBAgIQB8/MkWTfCxDNmuSVCKtrZDANBgkqhkiG9w0BAQsFADBN\n" +
            "MQswCQYDVQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMScwJQYDVQQDEx5E\n" +
            "aWdpQ2VydCBTSEEyIFNlY3VyZSBTZXJ2ZXIgQ0EwHhcNMTYwMTEzMDAwMDAwWhcN\n" +
            "MTkwMTE3MTIwMDAwWjBsMQswCQYDVQQGEwJERTEbMBkGA1UECBMSU2NobGVzd2ln\n" +
            "LUhvbHN0ZWluMRIwEAYDVQQHEwlBbHRlbmhvbHoxETAPBgNVBAoTCERhdGFwb3J0\n" +
            "MRkwFwYDVQQDExB0ZnMyLmRhdGFwb3J0LmRlMIIBIjANBgkqhkiG9w0BAQEFAAOC\n" +
            "AQ8AMIIBCgKCAQEAxny7IeEEqWYEVcGD+LDo6ocYQl6hNw+8vuHNzlJosaBg3LCW\n" +
            "+Gw2Ki+132sySTvueP8bGzb8bimO2fw/Ax9Dwpvt92pEGisCqRL9o/2U+Lyttm4m\n" +
            "gNH0k2QflCjfRIfYPT4pHz/bSIPo9L2JaMEoUMPiOLhkSeLdTu2pOfaglzQYiqbD\n" +
            "LhtBiovqtJ/zLtaKwQc/+mVVobe0Qf+o8NWfynJRP2kVd4Rw1vtx+9RQYQyl+mZN\n" +
            "y8Ksr2xon1MiYLCd0qxY9BMUWo1gct+rCqIrmJDrkpunWnTnKIny4+wWIZXMOCZb\n" +
            "+32qZ/rBaTXVXNlJQxUnAyFcelIuRFigyLbvNQIDAQABo4IB1zCCAdMwHwYDVR0j\n" +
            "BBgwFoAUD4BhHIIxYdUvKOeNRji0LOHG2eIwHQYDVR0OBBYEFPbtOD5mGrOshRtI\n" +
            "KoOdQKzPjJ9FMBsGA1UdEQQUMBKCEHRmczIuZGF0YXBvcnQuZGUwDgYDVR0PAQH/\n" +
            "BAQDAgWgMB0GA1UdJQQWMBQGCCsGAQUFBwMBBggrBgEFBQcDAjBrBgNVHR8EZDBi\n" +
            "MC+gLaArhilodHRwOi8vY3JsMy5kaWdpY2VydC5jb20vc3NjYS1zaGEyLWc1LmNy\n" +
            "bDAvoC2gK4YpaHR0cDovL2NybDQuZGlnaWNlcnQuY29tL3NzY2Etc2hhMi1nNS5j\n" +
            "cmwwTAYDVR0gBEUwQzA3BglghkgBhv1sAQEwKjAoBggrBgEFBQcCARYcaHR0cHM6\n" +
            "Ly93d3cuZGlnaWNlcnQuY29tL0NQUzAIBgZngQwBAgIwfAYIKwYBBQUHAQEEcDBu\n" +
            "MCQGCCsGAQUFBzABhhhodHRwOi8vb2NzcC5kaWdpY2VydC5jb20wRgYIKwYBBQUH\n" +
            "MAKGOmh0dHA6Ly9jYWNlcnRzLmRpZ2ljZXJ0LmNvbS9EaWdpQ2VydFNIQTJTZWN1\n" +
            "cmVTZXJ2ZXJDQS5jcnQwDAYDVR0TAQH/BAIwADANBgkqhkiG9w0BAQsFAAOCAQEA\n" +
            "Ov0b+vcKeCCC8m8yXnDalNGONJCHHDNIs1rtHyAPE8f/zUOK1jswhVV8vhVoj9av\n" +
            "9E7sS5iRha4nRnjVyM6yDoVtZum/OFGCFJIPkzt78RxRNQTkM2MuJ4sd8LOXn9Yp\n" +
            "x8+s5/IqYARMV7DKhePmm+CqjvDBgRHDqVWpqFadXLZsUhcMnxwV/rDuQ6ZtdXZY\n" +
            "GjksJAQnOKSMunuAUkHls+xwy2VrXOxsnEitYSoqzODpodjtZGDRNKokm9SfkQ1A\n" +
            "y6U9n9mWQbyDpP1K2hZQTKy+QQoIQmWejDYFVMnffUmNvrbV2xPBUNojz5FuBkrU\n" +
            "Sar7tfeq47/fq+ntdTDLbA==\n" +
            "-----END CERTIFICATE-----\n";
}