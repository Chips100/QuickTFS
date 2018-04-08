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