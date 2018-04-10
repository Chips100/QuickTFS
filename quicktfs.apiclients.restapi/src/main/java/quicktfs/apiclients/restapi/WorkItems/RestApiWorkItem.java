package quicktfs.apiclients.restapi.WorkItems;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a Work Item as it is transferred by the TFS Rest API.
 * Simple POJO, should only be used for deserialization of Responses.
 */
public class RestApiWorkItem {
    /**
     * ID of the Work Item.
     */
    public int id;

    /**
     * Fields of the Work Item.
     */
    public RestApiWorkItemFields fields;

    /**
     * Collection of fields that a Work Item can have.
     */
    public static class RestApiWorkItemFields {
        /**
         * Team project to which the Work Item belongs.
         */
        @SerializedName("System.TeamProject")
        public String teamProject;

        /**
         * Activity to which the Work Item belongs.
         */
        @SerializedName("Microsoft.VSTS.Common.Activity")
        public String activity;

        /**
         * Iteration Path in which the Work Item is planned.
         */
        @SerializedName("System.IterationPath")
        public String iterationPath;

        /**
         * State of the Work Item.
         */
        @SerializedName("System.State")
        public String state;

        /**
         * Type of the Work Item.
         */
        @SerializedName("System.WorkItemType")
        public String type;

        /**
         * Title of the Work Item.
         */
        @SerializedName("System.Title")
        public String title;

        /**
         * Description of the Work Item.
         */
        @SerializedName("System.Description")
        public String description;

        /**
         * Name of the user that the Work Item is assigned to.
         */
        @SerializedName("System.AssignedTo")
        public String assignedTo;
    }
}