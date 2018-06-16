package quicktfs.app.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import quicktfs.app.R;
import quicktfs.app.workItemDetails.WorkItemDetailsActivity;

/**
 * Home screen for logged in user with starting points for all operations.
 */
public class HomeActivity extends AppCompatActivity {
    // UI references.
    private EditText searchWorkItemIdEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Get UI references.
        searchWorkItemIdEditText = findViewById(R.id.homeSearchWorkItemId);
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
}