package quicktfs.apiclients.restapi.Authentication;

/**
 * Represents an identity that has successfully authenticated.
 */
public class AuthenticatedIdentity {
    private final String domain;
    private final String accountName;
    private final String displayName;
    private final String mailAddress;

    /**
     * Creates an AuthenticatedIdentity.
     * @param domain Name of the domain of the identity.
     * @param accountName Name of the account of the identity.
     * @param displayName User friendly display name of the identity.
     * @param mailAddress Email address of the identity.
     */
    public AuthenticatedIdentity(String domain, String accountName, String displayName, String mailAddress) {
        this.domain = domain;
        this.accountName = accountName;
        this.displayName = displayName;
        this.mailAddress = mailAddress;
    }

    /**
     * Gets the name of the domain of the identity.
     * @return The name of the domain of the identity.
     */
    public String getDomain() {
        return domain;
    }

    /**
     * Gets the name of the account of the identity.
     * @return The name of the account of the identity.
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * Gets the user friendly display name of the identity.
     * @return The user friendly display name of the identity.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the email address of the identity.
     * @return The email address of the identity.
     */
    public String getMailAddress() {
        return mailAddress;
    }
}