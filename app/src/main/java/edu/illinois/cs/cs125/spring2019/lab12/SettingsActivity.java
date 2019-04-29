package edu.illinois.cs.cs125.spring2019.lab12;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/** Settings. */
public class SettingsActivity extends AppCompatActivity {
    /**
     *
     * @param savedInstanceState is a state.
     */
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Button settingsBack = (Button) findViewById(R.id.settings_back);
        settingsBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
