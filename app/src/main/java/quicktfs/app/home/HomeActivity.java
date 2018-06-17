package quicktfs.app.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import quicktfs.apiclients.contracts.projects.ProjectClient;
import quicktfs.apiclients.contracts.projects.ProjectDto;
import quicktfs.app.IocContainerStub;
import quicktfs.app.R;
import quicktfs.app.projectHome.ProjectHomeActivity;
import quicktfs.app.workItemDetails.WorkItemDetailsActivity;

/**
 * Home screen for logged in user with starting points for all operations.
 */
public class HomeActivity extends AppCompatActivity {
    private ProjectClient projectClient;

    // UI references.
    private EditText searchWorkItemIdEditText;
    private ListView projectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // stub solution for IOC.
        projectClient = IocContainerStub.getInstance(ProjectClient.class);

        // Get UI references.
        searchWorkItemIdEditText = (EditText)findViewById(R.id.homeSearchWorkItemId);
        projectList = (ListView)findViewById(R.id.homeProjectList);

        // Load projects
        new LoadProjectsTask(this, projectClient).execute(new AsyncLoadProjectsTask.LoadProjectsParams());

        // Configure select Handler for Project List.
        projectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ProjectDto project = (ProjectDto)adapterView.getItemAtPosition(i);
                openProjectByName(project.getName());
            }
        });
    }

    /**
     * Opens a project by its name, selected by the user from the Project list.
     * @param projectName Name of the project to open.
     */
    public void openProjectByName(String projectName) {
        Intent intent = new Intent(this, ProjectHomeActivity.class);
        intent.putExtra(ProjectHomeActivity.INTENT_PROJECT_NAME, projectName);
        startActivity(intent);
    }

    /**
     * Opens a Work Item by its ID, specified by the user in the EditText.
     * @param view View that raised the event.
     */
    public void openWorkItemById(View view) {
        try {
            int workItemId = Integer.parseInt(searchWorkItemIdEditText.getText().toString());

            Intent intent = new Intent(this, WorkItemDetailsActivity.class);
            intent.putExtra(WorkItemDetailsActivity.INTENT_WORK_ITEM_ID, workItemId);
            startActivity(intent);
        }
        catch(NumberFormatException exception) {
            Toast.makeText(this,
                this.getString(R.string.homeSearchWorkItemIdMustBeNumberMessage), Toast.LENGTH_LONG).show();
        }
    }

    private static class LoadProjectsTask extends AsyncLoadProjectsTask {
        public LoadProjectsTask(HomeActivity context, ProjectClient client) { super(context, client); }

        @Override
        protected void handleSuccess(LoadProjectsParams loadProjectsParams, LoadProjectsResult loadProjectsResult) {
            final HomeActivity context = getContext();
            if (context == null) return;

            ProjectArrayAdapter adapter = new ProjectArrayAdapter(context);
            adapter.addAll(loadProjectsResult.getProjects());
            context.projectList.setAdapter(adapter);
        }
    }
}