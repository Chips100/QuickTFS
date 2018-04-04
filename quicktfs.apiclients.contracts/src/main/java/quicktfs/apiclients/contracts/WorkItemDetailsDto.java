package quicktfs.apiclients.contracts;

/**
 * Represents details of a TFS Work Item.
 */
public final class WorkItemDetailsDto {
    private final int id;
    private final String title;
    private final String description;

    /**
     * Creates a WorkItemDetailsDto.
     * @param id ID of the Work Item.
     * @param title Title of the Work Item.
     * @param description Description of the Work Item.
     */
    public WorkItemDetailsDto(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    /**
     * Gets the ID of the Work Item.
     * @return The ID of the Work Item.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the title of the Work Item.
     * @return The title of the Work Item.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the description of the Work Item.
     * @return The description of the Work Item.
     */
    public String getDescription() {
        return description;
    }
}