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

public class BasketballActivity extends AppCompatActivity {

    private int scoreTeamA = 0;
    private int scoreTeamB = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basketball);

        getSupportActionBar().setTitle("Basketball");

        // Set actionBar bar color
        ColorDrawable primary = new ColorDrawable(ContextCompat.getColor(this, R.color.colorPrimary_basketball));
        getSupportActionBar().setBackgroundDrawable(primary);

        // Set status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark_basketball));
        }

        // Show back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    public void teamA_threePoints(View view) {
        scoreTeamA += 3;
        displayForTeamA();
    }

    public void teamA_twoPoints(View view) {
        scoreTeamA += 2;
        displayForTeamA();
    }

    public void teamA_onePoint(View view) {
        scoreTeamA += 1;
        displayForTeamA();
    }

    public void teamB_threePoints(View view) {
        scoreTeamB += 3;
        displayForTeamB();
    }

    public void teamB_twoPoints(View view) {
        scoreTeamB += 2;
        displayForTeamB();
    }

    public void teamB_onePoint(View view) {
        scoreTeamB += 1;
        displayForTeamB();
    }

    public void resetGame(View view) {
        scoreTeamA = 0;
        scoreTeamB = 0;
        displayForTeamA();
        displayForTeamB();
    }

    public void resetGame() {
        scoreTeamA = 0;
        scoreTeamB = 0;
        displayForTeamA();
        displayForTeamB();
    }

    public void displayForTeamA() {
        TextView scoreView = (TextView) findViewById(R.id.basketball_team_a_score);
        scoreView.setText(String.valueOf(scoreTeamA));
    }

    public void displayForTeamB() {
        TextView scoreView = (TextView) findViewById(R.id.basketball_team_b_score);
        scoreView.setText(String.valueOf(scoreTeamB));
    }
}
