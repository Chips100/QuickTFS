package quicktfs.app.workItemDetails;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import quicktfs.apiclients.contracts.exceptions.ApiAccessException;
import quicktfs.apiclients.contracts.exceptions.EntityNotFoundException;
import quicktfs.apiclients.contracts.WorkItemAssignClient;
import quicktfs.apiclients.contracts.WorkItemDetailsDto;
import quicktfs.apiclients.contracts.WorkItemQueryClient;
import quicktfs.app.IocContainerStub;
import quicktfs.app.R;
import quicktfs.utilities.ExceptionUtilities;
import quicktfs.utilities.UiUtilities;

/**
 * Displays details of a single Work Item and
 * offers common operations on that Work Item.
 */
public class WorkItemDetailsActivity extends AppCompatActivity {
    public static final String INTENT_WORK_ITEM_ID = "WorkItemId";

    private int workItemId;
    private WorkItemQueryClient workItemQueryClient;
    private WorkItemAssignClient workItemAssignClient;

    // UI references.
    private TextView teamProjectTextView;
    private TextView iterationPathTextView;
    private TextView activityTextView;
    private TextView stateTextView;
    private TextView titleTextView;
    private TextView assignedToTextView;
    private TextView descriptionTextView;
    private View progressBar;
    private View detailsContainer;
    private View notFoundContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_item_details);

        // stub solution for IOC.
        workItemQueryClient = IocContainerStub.getInstance(WorkItemQueryClient.class);
        workItemAssignClient = IocContainerStub.getInstance(WorkItemAssignClient.class);

        // Get UI references.
        teamProjectTextView = (TextView)findViewById(R.id.workItemDetailsTeamProject);
        iterationPathTextView = (TextView)findViewById(R.id.workItemDetailsIterationPath);
        activityTextView = (TextView)findViewById(R.id.workItemDetailsActivity);
        stateTextView = (TextView)findViewById(R.id.workItemDetailsState);
        titleTextView = (TextView)findViewById(R.id.workItemDetailsTitle);
        assignedToTextView = (TextView)findViewById(R.id.workItemDetailsAssignedTo);
        descriptionTextView = (TextView)findViewById(R.id.workItemDetailsDescription);

        progressBar = findViewById(R.id.workItemDetailsProgressbar);
        detailsContainer = findViewById(R.id.workItemDetailsContainer);
        notFoundContainer = findViewById(R.id.workItemDetailsNotFound);


        // Read Work Item ID from intent that started the activity.
        workItemId = getIntent().getIntExtra(INTENT_WORK_ITEM_ID, 0);
        loadWorkItem();
    }

    /**
     * Loads the details of the current Work Item.
     */
    public void loadWorkItem() {
        setLoadingState(true);

        new LoadWorkItemTask(this, workItemQueryClient)
                .execute(new AsyncLoadWorkItemTask.LoadWorkItemParams(workItemId));
    }

    /**
     * Displays the details of the specified Work Item in the UI.
     * @param workItem Details of the Work Item do display.
     */
    private void displayWorkItem(WorkItemDetailsDto workItem) {
        String title = workItem.getType() + " " + String.valueOf(workItem.getId())
                + ": " + workItem.getTitle();

        teamProjectTextView.setText(workItem.getTeamProject());
        iterationPathTextView.setText(workItem.getIterationPath());
        activityTextView.setText(workItem.getActivity());
        stateTextView.setText(workItem.getState());
        titleTextView.setText(title);
        assignedToTextView.setText(workItem.getAssignedTo());
        descriptionTextView.setText(UiUtilities.fromHtml(workItem.getDescription()));
    }

    /**
     * Sets the loading state to show or hide a progress bar.
     * @param isLoading True, if some operation is currently loading; otherwise false.
     */
    private void setLoadingState(boolean isLoading) {
        detailsContainer.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    /**
     * Assigns the current Work Item to the current user.
     * @param view View in the UI that raised the event.
     */
    public void assignToMe(View view) {
        setLoadingState(true);
        new AssignToMeTask(this, workItemAssignClient)
                .execute(new AsyncAssignWorkItemToMeTask.AssignWorkItemToMeParams(workItemId));
    }

    private static class LoadWorkItemTask extends AsyncLoadWorkItemTask {
        public LoadWorkItemTask(WorkItemDetailsActivity context, WorkItemQueryClient client) {
            super(context, client);
        }

        @Override
        protected void handleComplete(LoadWorkItemParams params) {
            WorkItemDetailsActivity context = getContext();
            if (context == null) return;

            context.setLoadingState(false);
        }

        @Override
        protected void handleSuccess(LoadWorkItemParams params, LoadWorkItemResult result) {
            WorkItemDetailsActivity context = getContext();
            if (context == null) return;

            context.displayWorkItem(result.getWorkItem());
        }

        @Override
        protected void handleApiAccessException(LoadWorkItemParams params, ApiAccessException exception) {
            WorkItemDetailsActivity context = getContext();
            if (context == null) return;

            // Special handling when Work Item was not found.
            if (ExceptionUtilities.findCauseOfType(exception, EntityNotFoundException.class) != null) {
                context.detailsContainer.setVisibility(View.GONE);
                context.notFoundContainer.setVisibility(View.VISIBLE);
                return;
            }

            // Otherwise, fall back to default handling.
            super.handleApiAccessException(params, exception);
        }
    }

    private static class AssignToMeTask extends AsyncAssignWorkItemToMeTask {
        public AssignToMeTask(WorkItemDetailsActivity context, WorkItemAssignClient client) {
            super(context, client);
        }

        @Override
        protected void handleComplete(AssignWorkItemToMeParams params) {
            WorkItemDetailsActivity context = getContext();
            if (context == null) return;

            context.setLoadingState(false);
        }

        @Override
        protected void handleSuccess(AssignWorkItemToMeParams params, AssignWorkItemToMeResult assignWorkItemToMeResult) {
            WorkItemDetailsActivity context = getContext();
            if (context == null) return;

            Toast.makeText(context, context.getString(R.string.workItemDetailsAssignMeSuccessMessage), Toast.LENGTH_LONG).show();
        }
    }
}