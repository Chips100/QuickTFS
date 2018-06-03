package quicktfs.app.settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import quicktfs.app.AppConfigurationSource;
import quicktfs.app.R;

/**
 * Activity for viewing and changing configuration settings.
 */
public class SettingsActivity extends AppCompatActivity {
    private AppConfigurationSource configurationSource;

    // UI references.
    private EditText tfsUrlEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Get UI references.
        tfsUrlEditText = findViewById(R.id.settingsTfsUrl);

        configurationSource = AppConfigurationSource.getInstance();
        load();
    }

    @Override
    protected void onStop() {
        save();
        super.onStop();
    }

    private void load() {
        tfsUrlEditText.setText(configurationSource.getString("TFS_URL"));
    }

    private void save() {
        configurationSource.setString("TFS_URL", tfsUrlEditText.getText().toString());
    }
}