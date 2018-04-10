package quicktfs.apiclients.contracts;

/**
 * Represents details of a TFS Work Item.
 */
public final class WorkItemDetailsDto {
    private final int id;
    private final String teamProject;
    private final String type;
    private final String title;
    private final String description;
    private final String assignedTo;
    private final String activity;
    private final String iterationPath;
    private final String state;

    /**
     * Creates a WorkItemDetailsDto.
     * @param id ID of the Work Item.
     * @param teamProject Team project to which the Work Item belongs.
     * @param type Type of the Work Item.
     * @param title Title of the Work Item.
     * @param description Description of the Work Item.
     * @param assignedTo Name of the user that the Work Item is assigned to.
     * @param activity Activity to which the Work Item belongs.
     * @param iterationPath Iteration Path in which the Work Item is planned.
     * @param state State of the Work Item.
     */
    public WorkItemDetailsDto(int id, String teamProject, String type, String title,
        String description, String assignedTo, String activity, String iterationPath, String state) {
        this.id = id;
        this.teamProject = teamProject;
        this.type = type;
        this.title = title;
        this.description = description;
        this.assignedTo = assignedTo;
        this.activity = activity;
        this.iterationPath = iterationPath;
        this.state = state;
    }

    /**
     * Gets the ID of the Work Item.
     * @return The ID of the Work Item.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the team project to which the Work Item belongs.
     * @return The team project to which the Work Item belongs.
     */
    public String getTeamProject() {
        return teamProject;
    }

    /**
     * Gets the type of the Work Item.
     * @return The type of the Work Item.
     */
    public String getType() {
        return type;
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

    /**
     * Gets the name of the user that the Work Item is assigned to.
     * @return The name of the user that the Work Item is assigned to.
     */
    public String getAssignedTo() {
        return assignedTo;
    }

    /**
     * Gets the activity to which the Work Item belongs.
     * @return The activity to which the Work Item belongs.
     */
    public String getActivity() {
        return activity;
    }

    /**
     * Gets the iteration Path in which the Work Item is planned.
     * @return The iteration Path in which the Work Item is planned.
     */
    public String getIterationPath() {
        return iterationPath;
    }

    /**
     * Gets the state of the Work Item.
     * @return The state of the Work Item.
     */
    public String getState() {
        return state;
    }
}