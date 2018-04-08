package quicktfs.app;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

import quicktfs.apiclients.contracts.ConfigurationSource;
import quicktfs.utilities.StringUtilities;

/**
 * Source for configuration values in the Android App context.
 */
public class AppConfigurationSource implements ConfigurationSource {
    private static final String KEY_SETTINGS = "SettingsStore";
    private static SharedPreferences preferences;
    private static AppConfigurationSource instance = new AppConfigurationSource();

    // Private constructor to enforce singleton usage.
    private AppConfigurationSource() { }

    /**
     * Gets the singleton instance of AppConfigurationSource.
     * @return Gets the singleton instance of AppConfigurationSource.
     */
    public static AppConfigurationSource getInstance() {
        return instance;
    }

    /**
     * Gets a configurable String value.
     * @param key Key of the configuration setting.
     * @return The configured value for the setting.
     */
    @Override
    public String getString(String key) {
        return preferences.getString(key, null);
    }

    /**
     * Gets a configurable String collection value.
     * @param key Key of the configuration setting.
     * @return The configured value for the setting.
     */
    @Override
    public Set<String> getStringSet(String key) {
        return preferences.getStringSet(key, null);
    }

    /**
     * Sets a value for a String configuration setting.
     * @param key Key of the configuration setting.
     * @param value Value to set for the configuration setting.
     */
    public void setString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    /**
     * Sets values for a String collection configuration setting.
     * @param key Key of the configuration setting.
     * @param values Values to set for the configuration setting.
     */
    public void setStringSet(String key, Set<String> values) {
        preferences.edit().putStringSet(key, values).apply();
    }

    /**
     * Initializes the AppConfigurationSource with the specified context.
     * Required to gain access to the shared preferences via a context.
     * @param context Context to initialize the AppConfigurationSource.
     */
    public void init(Context context) {
        preferences = context.getSharedPreferences(KEY_SETTINGS, Context.MODE_PRIVATE);
        initDataportDefaults();
    }


    private void initDataportDefaults() {
        // Only initialize if no settings have been provided yet.
        if (!StringUtilities.isNullOrEmpty(getString("TFS_URL"))) return;

        setString("TFS_URL", "https://tfs2.dataport.de/tfs_2/CCSE/");
        Set<String> set = new HashSet<>();
        set.add(DataportCertificate);
        setStringSet("TRUSTED_CERTIFICATES", set);
    }

    private final static String DataportCertificate
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