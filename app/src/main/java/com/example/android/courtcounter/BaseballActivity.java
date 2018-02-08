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

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.stream.IntStream;

public class BaseballActivity extends AppCompatActivity {

    // Constants
    private static final int TEAMS = 2;
    private static final int OUTS_PER_HALF = 3;
    private static final int OUTS_PER_INNING = TEAMS * OUTS_PER_HALF;
    private static final int INNINGS_PER_GAME = 9;
    private static final int OUTS_PER_GAME = OUTS_PER_INNING * INNINGS_PER_GAME;
    private static final int BALLS_PER_WALK = 4;
    private static final int STRIKES_PER_OUT = 3;

    // Scores (per inning)
    private int[] scoreTeamA = new int[9];
    private int[] scoreTeamB = new int[9];

    // Half. 0 is the top of an inning (Team A), 1 is the bottom of an inning (Team B)
    private byte half = 0;

    // Game stats
    private int balls = 0;
    private int strikes = 0;
    private int outs = 0; // Outs in the whole game. See the method outs_inThisInning().

    // References to Views
    private TextView[] scoreBoardTeamA = new TextView[9];
    private TextView[] scoreBoardTeamB = new TextView[9];
    private TextView[] scoreBoardTotalScores = new TextView[2];
    private TextView scoreBoard_TotalLabel;
    private TextView scoreBoard_balls;
    private TextView scoreBoard_strikes;
    private TextView scoreBoard_outs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baseball);

        getSupportActionBar().setTitle("Baseball");

        //Set actionBar bar color
        ColorDrawable primary = new ColorDrawable(ContextCompat.getColor(this, R.color.colorPrimary_baseball));
        getSupportActionBar().setBackgroundDrawable(primary);

        //Set status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark_baseball));
        }

        // Show back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get View references
        getViewReferences();

        resetGame();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putIntArray("scoreTeamA", scoreTeamA);
        savedInstanceState.putIntArray("scoreTeamB", scoreTeamB);
        savedInstanceState.putByte("half", half);
        savedInstanceState.putInt("balls", balls);
        savedInstanceState.putInt("strikes", strikes);
        savedInstanceState.putInt("outs", outs);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        scoreTeamA = savedInstanceState.getIntArray("scoreTeamA");
        scoreTeamB = savedInstanceState.getIntArray("scoreTeamB");
        half = savedInstanceState.getByte("half");
        balls = savedInstanceState.getInt("balls");
        strikes = savedInstanceState.getInt("strikes");
        outs = savedInstanceState.getInt("outs");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_baseball, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reset:
                resetGame();
                return true;
            case R.id.action_inning_minus:
                inning_minus();
                return true;
            case R.id.action_inning_plus:
                inning_plus();
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

    // Get references to all TextViews
    private void getViewReferences() {
        // Team A scoreboard (per-inning scores)
        scoreBoardTeamA[0] = findViewById(R.id.baseball_team_a_inning_1);
        scoreBoardTeamA[1] = findViewById(R.id.baseball_team_a_inning_2);
        scoreBoardTeamA[2] = findViewById(R.id.baseball_team_a_inning_3);
        scoreBoardTeamA[3] = findViewById(R.id.baseball_team_a_inning_4);
        scoreBoardTeamA[4] = findViewById(R.id.baseball_team_a_inning_5);
        scoreBoardTeamA[5] = findViewById(R.id.baseball_team_a_inning_6);
        scoreBoardTeamA[6] = findViewById(R.id.baseball_team_a_inning_7);
        scoreBoardTeamA[7] = findViewById(R.id.baseball_team_a_inning_8);
        scoreBoardTeamA[8] = findViewById(R.id.baseball_team_a_inning_9);

        // Team B scoreboard (per-inning scores)
        scoreBoardTeamB[0] = findViewById(R.id.baseball_team_b_inning_1);
        scoreBoardTeamB[1] = findViewById(R.id.baseball_team_b_inning_2);
        scoreBoardTeamB[2] = findViewById(R.id.baseball_team_b_inning_3);
        scoreBoardTeamB[3] = findViewById(R.id.baseball_team_b_inning_4);
        scoreBoardTeamB[4] = findViewById(R.id.baseball_team_b_inning_5);
        scoreBoardTeamB[5] = findViewById(R.id.baseball_team_b_inning_6);
        scoreBoardTeamB[6] = findViewById(R.id.baseball_team_b_inning_7);
        scoreBoardTeamB[7] = findViewById(R.id.baseball_team_b_inning_8);
        scoreBoardTeamB[8] = findViewById(R.id.baseball_team_b_inning_9);

        scoreBoard_TotalLabel = findViewById(R.id.total_label);

        scoreBoardTotalScores[0] = findViewById(R.id.baseball_team_a_total_score);
        scoreBoardTotalScores[1] = findViewById(R.id.baseball_team_b_total_score);

        scoreBoard_balls = findViewById(R.id.baseball_balls);
        scoreBoard_strikes = findViewById(R.id.baseball_strikes);
        scoreBoard_outs = findViewById(R.id.baseball_outs);
    }

    public void inning_minus() {
        // Remove outs until we reach the previous inning
        int previousInning = currentInning();

        while (currentInning() == previousInning) {
            outs--;
            // Set current inning border to gray. This will leave the previous inning gray when we change innings.
            setColor_currentInning(R.drawable.rectangle_gray);
            if (outs < 0) {
                outs = 0;
                setColor_currentInning(R.drawable.rectangle_red);
                return;
            }
        }

        // Remove two additional outs so we're at the beginning of the inning
        outs -= 2;

        // Do not let outs go below 0
        if (outs < 0) {
            outs = 0;
        }

        scoreBoard_TotalLabel.setText(R.string.total_text);

        // Reset current batter stats
        balls = 0;
        strikes = 0;

        // Update batter stats TextViews
        scoreBoard_balls.setText(String.valueOf(balls));
        scoreBoard_strikes.setText(String.valueOf(strikes));

        // Current inning border is red
        setColor_currentInning(R.drawable.rectangle_red);

        // Update scoreboard outs TextView
        scoreBoard_outs.setText(String.valueOf(outs_inThisInning()));
    }

    public void inning_plus() {
        // Exit if the game is over
        if (currentInning() >= 9.0) {
            return;
        }

        //Add outs until we reach the next inning
        int previousInning = currentInning();

        while (currentInning() == previousInning) {
            outs++;
            // Set current inning border to gray. This will leave the previous inning gray when we change innings.
            if (currentInning() >= 9.0) {
                scoreBoard_TotalLabel.setText(R.string.final_text);
                return;
            }
            setColor_currentInning(R.drawable.rectangle_gray);
        }


        // Current inning border is red
        setColor_currentInning(R.drawable.rectangle_red);
        // Update scoreboard outs TextView
        scoreBoard_outs.setText(String.valueOf(outs_inThisInning()));
    }

    // Batter gets a ball. If there are 4 balls, reset Balls and Strikes for the next batter.
    public void ball(View view) {
        // Do nothing if the game is over
        if (currentInning() >= 9.0) {
            return;
        }

        balls++;

        if (balls >= BALLS_PER_WALK) {
            balls = 0;
            strikes = 0;
        }

        scoreBoard_balls.setText(String.valueOf(balls));
    }

    // Batter gets a strike. If there are 3 strikes, reset Balls and Strikes, add an Out.
    public void strike(View view) {
        // Do nothing if the game is over
        if (currentInning() >= 9.0) {
            return;
        }

        strikes++;

        if (strikes >= STRIKES_PER_OUT) {
            strikes = 0;
            balls = 0;
            out(view);
            return;
        }

        // Update TextView
        scoreBoard_strikes.setText(String.valueOf(strikes));
    }

    // Batter hits the ball. Reset Balls and Strikes.
    public void hit(View view) {
        // Do nothing if the game is over
        if (currentInning() >= 9.0) {
            return;
        }

        strikes = 0;
        balls = 0;
    }

    // Player scores a run. Update team score for the current inning.
    public void scoreRun(View view) {
        // Do nothing if the game is over
        if (currentInning() >= 9.0) {
            return;
        }

        // Update score
        switch (half) {
            case 0:
                scoreTeamA[currentInning()]++;
                break;
            case 1:
                scoreTeamB[currentInning()]++;
                break;
        }

        // Set Team A TextViews
        for (int a = 0; a < scoreTeamA.length; a++) {
            scoreBoardTeamA[a].setText(String.valueOf(scoreTeamA[a]));
        }

        // Set Team B TextViews
        for (int a = 0; a < scoreTeamA.length; a++) {
            scoreBoardTeamA[a].setText(String.valueOf(scoreTeamA[a]));
        }

        // Update total TextViews
        scoreBoardTotalScores[0].setText(String.valueOf(getSum_ofArray(scoreTeamA)));
        scoreBoardTotalScores[1].setText(String.valueOf(getSum_ofArray(scoreTeamB)));
    }

    private int getSum_ofArray(int thisArray[]) {
        int result = 0;

        for (int i : thisArray) {
            result += i;
        }

        return result;
    }

    // Batter or Runner is out. Go to next (half) inning if there are 3 Outs.
    public void out(View view) {
        // Do nothing if the game is over
        if (currentInning() >= 9.0) {
            return;
        }

        // Set current inning border to gray. This will leave the previous inning gray when we change innings.
        setColor_currentInning(R.drawable.rectangle_gray);

        outs++;

        if (outs_inThisInning() == 0) {
            // Go to next half inning (bitwise flip)
            half ^= 1;

            // Reset stats for current batter
            balls = 0;
            strikes = 0;
        }

        // See if we're at the end of the game
        if (currentInning() >= 9.0) {
            // (not sure what to do about this)
            return;
        }

        // Current inning border is red
        setColor_currentInning(R.drawable.rectangle_red);

        // Update scoreboard outs TextView
        scoreBoard_outs.setText(String.valueOf(outs_inThisInning()));

        // Update scoreboard batter stats
        scoreBoard_strikes.setText(String.valueOf(strikes));
        scoreBoard_balls.setText(String.valueOf(balls));
    }

    private void setColor_currentInning(int drawableID) {
        switch (half) {
            case 0:
                // Top of inning, Team A
                scoreBoardTeamA[currentInning()].setBackgroundResource(drawableID);
                break;
            case 1:
                // Bottom of inning, Team B
                scoreBoardTeamB[currentInning()].setBackgroundResource(drawableID);
                break;
        }
    }

    private int outs_inThisInning() {
        return outs % OUTS_PER_HALF;
    }

    // 0-based inning number
    private int currentInning() {
        return outs / OUTS_PER_INNING;
    }

    public void resetGame() {
        scoreTeamA = new int[9];
        scoreTeamB = new int[9];

        half = 0;
        balls = 0;
        strikes = 0;
        outs = 0;

        for (int i = 0; i < scoreBoardTeamA.length; i++) {
            // Set every scoreboard border to grey
            scoreBoardTeamA[i].setBackgroundResource(R.drawable.rectangle_gray);
            scoreBoardTeamB[i].setBackgroundResource(R.drawable.rectangle_gray);

            // Update all scoreboard TextViews
            scoreBoardTeamA[i].setText(String.valueOf(scoreTeamA[i]));
            scoreBoardTeamB[i].setText(String.valueOf(scoreTeamB[i]));
        }

        // Update total TextViews
        scoreBoardTotalScores[0].setText(String.valueOf(getSum_ofArray(scoreTeamA)));
        scoreBoardTotalScores[1].setText(String.valueOf(getSum_ofArray(scoreTeamB)));

        // Update scoreboard batter stats TextViews
        scoreBoard_strikes.setText(String.valueOf(strikes));
        scoreBoard_balls.setText(String.valueOf(balls));

        // Set the current inning border color to red
        setColor_currentInning(R.drawable.rectangle_red);

        // Set the total score label to "Total" (instead of "Final")
        scoreBoard_TotalLabel.setText(R.string.total_text);
    }

}
