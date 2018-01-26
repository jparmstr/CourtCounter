package com.example.android.courtcounter;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class SoccerActivity extends AppCompatActivity {

    private int scoreTeamA = 0;
    private int sogTeamA = 0;
    private int foulsTeamA = 0;

    private int scoreTeamB = 0;
    private int sogTeamB = 0;
    private int foulsTeamB = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soccer);

        getSupportActionBar().setTitle("Soccer");

        //Set actionBar bar color
        ColorDrawable primary = new ColorDrawable(ContextCompat.getColor(this, R.color.colorPrimary_soccer));
        getSupportActionBar().setBackgroundDrawable(primary);

        //Set status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark_soccer));
        }

        // Show back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        updateDisplay();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt("scoreTeamA", scoreTeamA);
        savedInstanceState.putInt("scoreTeamB", scoreTeamB);
        savedInstanceState.putInt("sogTeamA", sogTeamA);
        savedInstanceState.putInt("sogTeamB", sogTeamB);
        savedInstanceState.putInt("foulsTeamA", foulsTeamA);
        savedInstanceState.putInt("foulsTeamB", foulsTeamB);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        scoreTeamA = savedInstanceState.getInt("scoreTeamA");
        scoreTeamB = savedInstanceState.getInt("scoreTeamB");
        sogTeamA = savedInstanceState.getInt("sogTeamA");
        sogTeamB = savedInstanceState.getInt("sogTeamB");
        foulsTeamA = savedInstanceState.getInt("foulsTeamA");
        foulsTeamB = savedInstanceState.getInt("foulsTeamB");
        

        updateDisplay();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reset:
                resetGame();
                return true;
            case android.R.id.home:
                goBackToMainActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goBackToMainActivity() {
        // Switch to the Main Activity
        Intent showMainActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(showMainActivity);
    }

    public void teamA_SoG(View view) {
        sogTeamA++;
        updateDisplay();
    }

    public void teamA_goal(View view) {
        scoreTeamA++;
        updateDisplay();
    }

    public void teamA_foul(View view) {
        foulsTeamA++;
        updateDisplay();
    }

    public void teamB_SoG(View view) {
        sogTeamB++;
        updateDisplay();
    }

    public void teamB_goal(View view) {
        scoreTeamB++;
        updateDisplay();
    }

    public void teamB_foul(View view) {
        foulsTeamB++;
        updateDisplay();
    }

    public void resetGame() {
        scoreTeamA = 0;
        sogTeamA = 0;
        foulsTeamA = 0;

        scoreTeamB = 0;
        sogTeamB = 0;
        foulsTeamB = 0;

        updateDisplay();
    }

    // Update all TextViews
    public void updateDisplay() {
        // Team A Score
        TextView scoreView_A = (TextView) findViewById(R.id.soccer_team_a_score);
        scoreView_A.setText(String.valueOf(scoreTeamA));

        // Team A Shots on Goal
        TextView sogView_A = (TextView) findViewById(R.id.soccer_team_a_sog);
        sogView_A.setText("SoG: " + String.valueOf(sogTeamA));

        // Team A Fouls
        TextView foulsView_A = (TextView) findViewById(R.id.soccer_team_a_fouls);
        foulsView_A.setText("Fouls: " + String.valueOf(foulsTeamA));

        // Team B Score
        TextView scoreView_B = (TextView) findViewById(R.id.soccer_team_b_score);
        scoreView_B.setText(String.valueOf(scoreTeamB));

        // Team B Shots on Goal
        TextView sogView_B = (TextView) findViewById(R.id.soccer_team_b_sog);
        sogView_B.setText("SoG: " + String.valueOf(sogTeamB));

        // Team B Fouls
        TextView foulsView_B = (TextView) findViewById(R.id.soccer_team_b_fouls);
        foulsView_B.setText("Fouls: " + String.valueOf(foulsTeamB));
    }
}
