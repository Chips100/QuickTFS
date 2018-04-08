package quicktfs.apiclients.contracts;

import java.util.Set;

/**
 * Source for looking up configuration values.
 */
public interface ConfigurationSource {
    /**
     * Gets a configurable String value.
     * @param key Key of the configuration setting.
     * @return The configured value for the setting.
     */
    String getString(String key);

    /**
     * Gets a configurable String collection value.
     * @param key Key of the configuration setting.
     * @return The configured value for the setting.
     */
    Set<String> getStringSet(String key);
}