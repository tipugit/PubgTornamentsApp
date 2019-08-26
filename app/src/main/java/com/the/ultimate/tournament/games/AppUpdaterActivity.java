package com.the.ultimate.tournament.games;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.the.ultimate.tournament.games.R;
import com.the.ultimate.tournament.games.config.config;

public class AppUpdaterActivity extends AppCompatActivity {

    private TextView forceUpdateNote;
    private final String isForceUpdate = "true";
    private Button later;
    private String latestVersion;
    private TextView newVersion;
    private Button update;
    private TextView updateDate;
    private String updatedOn;
    private TextView whatsNew;
    private String whatsNewData;

    public AppUpdaterActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_updater);

        updateDate = (TextView) findViewById(R.id.date);
        newVersion = (TextView) findViewById(R.id.version);
        whatsNew = (TextView) findViewById(R.id.whatsnew);
        forceUpdateNote = (TextView) findViewById(R.id.forceUpdateNote);
        later = (Button) findViewById(R.id.laterButton);
        update = (Button) findViewById(R.id.updateButton);
        updateDate.setText(updatedOn);
        newVersion.setText(latestVersion);
        whatsNew.setText(whatsNewData);
        if (isForceUpdate.equals("true")) {
            later.setVisibility(View.GONE);
            forceUpdateNote.setVisibility(View.VISIBLE);
        }
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(config.mainurl+"app.apk"));
                startActivity(browserIntent);
            }
        });
    }
}
