package quicktfs.app.settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import quicktfs.app.R;

/**
 * Activity for viewing and changing configuration settings.
 */
public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
}