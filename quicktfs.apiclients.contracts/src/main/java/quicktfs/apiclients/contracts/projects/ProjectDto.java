package quicktfs.apiclients.contracts.projects;

/**
 * Represents details of a TFS Project.
 */
public class ProjectDto {
    private final String name;
    private final String description;

    /**
     * Creates a ProjectDto.
     * @param name Name of the Project.
     * @param description Description of the Project.
     */
    public ProjectDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Gets the name of the Project.
     * @return The name of the Project.
     */
    public String getName() { return name; }

    /**
     * Gets the description of the Project.
     * @return The description of the Project.
     */
    public String getDescription() { return description; }
}