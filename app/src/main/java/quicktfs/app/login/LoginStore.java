package quicktfs.app.login;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Stores login data persistently.
 */
public final class LoginStore {
    private final String KEY_PREFERENCES = "LoginDataStore";
    private final String KEY_DOMAIN = "Domain";
    private final String KEY_USERNAME = "UserName";
    private final String KEY_PASSWORD = "Password";
    private final SharedPreferences preferences;

    /**
     * Creates a LoginStore for persistent login data.
     * @param context Context of the LoginStore.
     */
    public LoginStore(Context context) {
        if (context == null) throw new IllegalArgumentException("context");

        // Refer to SharedPreferences for storing.
        this.preferences = context.getSharedPreferences(KEY_PREFERENCES, Context.MODE_PRIVATE);
    }

    /**
     * Stores the specified login data for later reuse.
     * @param domain Domain used for the login.
     * @param username Username used for the login.
     * @param password Password used for the login.
     */
    public void storeLoginData(String domain, String username, String password) {
        // Currently just skip the password.
        // Might be nice, but seems like a bad idea. Reconsider later.
        preferences.edit()
            .putString(KEY_DOMAIN, domain)
            .putString(KEY_USERNAME, username)
            .apply();
    }

    /**
     * Gets the domain of the stored login data; or an empty string if no login data was stored yet.
     * @return The domain of the stored login data.
     */
    public String getDomain() { return preferences.getString(KEY_DOMAIN, ""); }

    /**
     * Gets the username of the stored login data; or an empty string if no login data was stored yet.
     * @return The username of the stored login data.
     */
    public String getUsername() { return preferences.getString(KEY_USERNAME, ""); }

    /**
     * Gets the password of the stored login data; or an empty string if no login data was stored yet.
     * THE PASSWORD WILL CURRENTLY NEVER BE STORED; ALWAYS RETURNS EMPTY STRING:
     * @return The password of the stored login data.
     */
    public String getPassword() { return preferences.getString(KEY_PASSWORD, ""); }
}