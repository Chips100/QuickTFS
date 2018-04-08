package quicktfs.app.workItemDetails;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import quicktfs.apiclients.contracts.WorkItemAssignClient;
import quicktfs.apiclients.contracts.WorkItemDetailsDto;
import quicktfs.apiclients.contracts.WorkItemQueryClient;
import quicktfs.app.IocContainerStub;
import quicktfs.app.R;

/**
 * Displays details of a single Work Item and
 * offers common operations on that Work Item.
 */
public class WorkItemDetailsActivity extends AppCompatActivity {
    public static final String INTENT_WORKITEM_ID = "WorkItemId";

    private int workItemId;
    private WorkItemQueryClient workItemQueryClient;
    private WorkItemAssignClient workItemAssignClient;

    // UI references.
    private TextView titleTextView;
    private TextView descriptionTextView;
    private View progressBar;
    private View detailsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_item_details);

        // stub solution for IOC.
        workItemQueryClient = IocContainerStub.getDefaultInstance().getInstance(WorkItemQueryClient.class);
        workItemAssignClient = IocContainerStub.getDefaultInstance().getInstance(WorkItemAssignClient.class);

        // Get UI references.
        titleTextView = (TextView)findViewById(R.id.workItemDetailsTitle);
        descriptionTextView = (TextView)findViewById(R.id.workItemDetailsDescription);
        progressBar = findViewById(R.id.workItemDetailsProgressbar);
        detailsContainer = findViewById(R.id.workItemDetailsContainer);

        // Read Work Item ID from intent that started the activity.
        workItemId = getIntent().getIntExtra(INTENT_WORKITEM_ID, 0);
        loadWorkItem();
    }

    /**
     * Loads the details of the current Work Item.
     */
    public void loadWorkItem() {
        setLoadingState(true);

        new AsyncLoadWorkItemTask(this, workItemQueryClient) {
            @Override
            protected void handleComplete() {
                WorkItemDetailsActivity.this.setLoadingState(false);
            }

            @Override
            protected void handleSuccess(LoadWorkItemResult result) {
                WorkItemDetailsActivity.this.displayWorkItem(result.getWorkItem());
            }
        }.execute(new AsyncLoadWorkItemTask.LoadWorkItemParams(workItemId));
    }

    /**
     * Displays the details of the specified Work Item in the UI.
     * @param workItem Details of the Work Item do display.
     */
    private void displayWorkItem(WorkItemDetailsDto workItem) {
        titleTextView.setText(workItem.getTitle());
        descriptionTextView.setText(workItem.getDescription());
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
        new AsyncAssignWorkItemToMeTask(this, workItemAssignClient) {
            @Override
            protected void handleComplete() {
                WorkItemDetailsActivity.this.setLoadingState(false);
            }

            @Override
            protected void handleSuccess(AssignWorkItemToMeResult assignWorkItemToMeResult) {
                WorkItemDetailsActivity context = WorkItemDetailsActivity.this;
                Toast.makeText(context, context.getString(R.string.workItemDetailsAssignMeSuccessMessage), Toast.LENGTH_LONG).show();
            }
        }.execute(new AsyncAssignWorkItemToMeTask.AssignWorkItemToMeParams(workItemId));
    }
}