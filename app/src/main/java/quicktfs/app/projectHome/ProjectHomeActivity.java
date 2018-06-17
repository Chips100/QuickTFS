package quicktfs.app.projectHome;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import quicktfs.app.R;

/**
 * Home screen for the context of a project.
 * Serves as the landing page after selecting a TFS Project.
 */
public class ProjectHomeActivity extends AppCompatActivity {
    public static final String INTENT_PROJECT_NAME = "ProjectName";

    private String projectName;

    // UI references.
    private TextView projectNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_home);

        // stub solution for IOC.

        // Get UI references.
        projectNameTextView = (TextView)findViewById(R.id.projectHomeProjectName);

        // Read Project Name from intent that started the activity.
        projectName = getIntent().getStringExtra(INTENT_PROJECT_NAME);

        projectNameTextView.setText(projectName);
    }
}
