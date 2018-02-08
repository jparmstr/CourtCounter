package com.example.android.courtcounter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BaseballActivity_ganellon extends AppCompatActivity {

    /** GLOBAL CONSTANTS  **/
    private static final int TEAMS = 2;
    private static final int OUTS_PER_HALF = 3;
    private static final int OUTS_PER_INNING = TEAMS * OUTS_PER_HALF;
    private static final int INNINGS_PER_GAME = 9;
    private static final int OUTS_PER_GAME = OUTS_PER_INNING * INNINGS_PER_GAME;
    private static final int BALLS_PER_WALK = 4;
    private static final int STRIKES_PER_OUT = 3;
    // this is used to index / find the TextView for balls and strikes
    private enum batterStats {BALLS, STRIKES}
    // this is used to index / find the winner of the game
    private enum teams {TEAMA, TEAMB}

    /** GLOBAL VARIABLES  **/
    // two dimensional array with scores for each team
    private int[][] score;

    private TextView[] scoreBoardTeamA = new TextView[INNINGS_PER_GAME];
    private TextView[] scoreBoardTeamB = new TextView[INNINGS_PER_GAME];
    private TextView[][] scoreBoard_bothTeams = new TextView[][] {scoreBoardTeamA, scoreBoardTeamB};

    private TextView[] inningNumberTextViews = new TextView[INNINGS_PER_GAME];
    private TextView[] batterTextViews = new TextView[batterStats.values().length];
    private TextView[] totalScoreTextViews = new TextView[TEAMS];
    private Button[] buttons = new Button[5];
    private TextView outsLabel;

    // binary that determines which half of the inning we're in, always 0 or 1
    private int half = 0;
    // stats for current batter
    private int balls = 0;
    private int strikes = 0;
    // number of outs in the game so far -- NOTE: this is a running total!
    private int outs = 0;

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

        // reset everything that needs to be reset for a new game
        fillControlArrays();
        newGame();
    }

    // set up menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_baseball, menu);
        return true;
    }

    // wire up the New Game menu item to the newGame() method
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reset:
                newGame();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Need to add savedInstanceState methods again

    // reset the score array, global variables
    private void newGame() {
        score = new int[TEAMS][INNINGS_PER_GAME];

        // reset the inning, outs, and batter stats
        half = 0;
        outs = 0;
        resetBatter();
        setInitialDisplay();
        for (Button button : buttons) {
            button.setTextColor(Color.WHITE);
            button.setEnabled(true);
        }
        TextView view = findViewById(R.id.total_label);
        view.setText("Total");
    }

    // reset all the control elements to the default values
    private void setInitialDisplay() {
        // set all the rectangles to grey and texts to 0 for inning scores
        for (int i = 0; i < TEAMS; i++) {
            for (TextView control : scoreBoard_bothTeams[i]) {
                control.setText("0");
                control.setBackgroundResource(R.drawable.rectangle_gray);
            }
        }

        // set the current inning half to red border
        scoreBoard_bothTeams[half][currentInning()].setBackgroundResource(R.drawable.rectangle_red);

        // set all the innings labels back to default in case prior game had extra innings
        for (int i = 0; i < INNINGS_PER_GAME; i++) {
            inningNumberTextViews[i].setText(String.valueOf(i+1));
        }

        // reset the batter stats
        for (TextView batterStat : batterTextViews) {
            batterStat.setText("0");
        }

        // reset the score totals
        for (TextView score : totalScoreTextViews) {
            score.setText("0");
        }

        // set outs to 0
        outsLabel.setText("0");
    }

    // add the TextView elements to the appropriate arrays
    private void fillControlArrays() {
        // Add all Team A Score TextViews
        scoreBoardTeamA[0] = findViewById(R.id.baseball_team_a_inning_1);
        scoreBoardTeamA[1] = findViewById(R.id.baseball_team_a_inning_2);
        scoreBoardTeamA[2] = findViewById(R.id.baseball_team_a_inning_3);
        scoreBoardTeamA[3] = findViewById(R.id.baseball_team_a_inning_4);
        scoreBoardTeamA[4] = findViewById(R.id.baseball_team_a_inning_5);
        scoreBoardTeamA[5] = findViewById(R.id.baseball_team_a_inning_6);
        scoreBoardTeamA[6] = findViewById(R.id.baseball_team_a_inning_7);
        scoreBoardTeamA[7] = findViewById(R.id.baseball_team_a_inning_8);
        scoreBoardTeamA[8] = findViewById(R.id.baseball_team_a_inning_9);

        // Add all Team B Score TextViews
        scoreBoardTeamB[0] = findViewById(R.id.baseball_team_b_inning_1);
        scoreBoardTeamB[1] = findViewById(R.id.baseball_team_b_inning_2);
        scoreBoardTeamB[2] = findViewById(R.id.baseball_team_b_inning_3);
        scoreBoardTeamB[3] = findViewById(R.id.baseball_team_b_inning_4);
        scoreBoardTeamB[4] = findViewById(R.id.baseball_team_b_inning_5);
        scoreBoardTeamB[5] = findViewById(R.id.baseball_team_b_inning_6);
        scoreBoardTeamB[6] = findViewById(R.id.baseball_team_b_inning_7);
        scoreBoardTeamB[7] = findViewById(R.id.baseball_team_b_inning_8);
        scoreBoardTeamB[8] = findViewById(R.id.baseball_team_b_inning_9);

        // Add all the inning labels (for extra innings)
        inningNumberTextViews[0] = findViewById(R.id.inningLabel1);
        inningNumberTextViews[1] = findViewById(R.id.inningLabel2);
        inningNumberTextViews[2] = findViewById(R.id.inningLabel3);
        inningNumberTextViews[3] = findViewById(R.id.inningLabel4);
        inningNumberTextViews[4] = findViewById(R.id.inningLabel5);
        inningNumberTextViews[5] = findViewById(R.id.inningLabel6);
        inningNumberTextViews[6] = findViewById(R.id.inningLabel7);
        inningNumberTextViews[7] = findViewById(R.id.inningLabel8);
        inningNumberTextViews[8] = findViewById(R.id.inningLabel9);

        // Add the TextViews for balls and strikes | current batter
        batterTextViews[batterStats.BALLS.ordinal()] = findViewById(R.id.baseball_balls);
        batterTextViews[batterStats.STRIKES.ordinal()] = findViewById(R.id.baseball_strikes);

        // Add the TextViews for the total scores
        totalScoreTextViews[teams.TEAMA.ordinal()] = findViewById(R.id.baseball_team_a_total_score);
        totalScoreTextViews[teams.TEAMB.ordinal()] = findViewById(R.id.baseball_team_b_total_score);

        // Add the TextView for current outs per half
        outsLabel = findViewById(R.id.baseball_outs);

        // Add the Button controls for Game Over
        buttons[0] = findViewById(R.id.ball_button);
        buttons[1] = findViewById(R.id.strike_button);
        buttons[2] = findViewById(R.id.hit_button);
        buttons[3] = findViewById(R.id.score_button);
        buttons[4] = findViewById(R.id.out_button);

    }

    // Batter gets a ball. If there are 4 balls, reset Balls and Strikes for the next batter.
    public void ball(View view) {
        if (++balls == BALLS_PER_WALK) {
            resetBatter();
        }
        updateBatterDisplay();
    }

    // Batter gets a strike. If there are 3 strikes, reset Balls and Strikes, add an Out.
    public void strike(View view) {
        if (++strikes == STRIKES_PER_OUT) {
            resetBatter();
            out(null);
        }
        else updateBatterDisplay();
    }

    // Batter hits the ball. Reset Balls and Strikes.
    public void hit(View view) {
        resetBatter();
    }

    // Player scores a run. Update team score for the current inning.
    public void scoreRun(View view) {
        // Do nothing if the game is over
        ++score[half][currentInning()];
        updateScore();
    }

    // Batter or Runner is out. Go to next (half) inning if there are 3 Outs.
    public void out(View view) {
        unsetBorderColor();
        ++outs;
        updateOuts();

        // check to see if game is over
        if ((outs >= OUTS_PER_GAME) && !gameTied()) {
            gameOver();
            return;
        }

        // Should be handling Extra Innings here (app crashes due to this being unhandled)
        // resize score arrays
        // change inning labels (instead of 1-9, 10-18)

        if (outs_inThisInning() == 0) {
            // Go to next (half) inning
            half ^= 1;
            resetBatter();
        }
        setBorderColor();
        updateBatterDisplay();
    }

    // update the inning half score and total score
    private void updateScore() {
        int index = currentInning();
        scoreBoard_bothTeams[half][index].setText(String.valueOf(score[half][index]));
        int totalScore_forTeamAtBat = 0;
        for (int i = 0; i < score[half].length; i++) {
            totalScore_forTeamAtBat += score[half][i];
        }
        totalScoreTextViews[half].setText(String.valueOf(totalScore_forTeamAtBat));
        // don't update anything except the score
    }

    // reset the variables to 0, update the ball/strikes TextViews
    private void resetBatter() {
        balls = 0;
        strikes = 0;
        updateBatterDisplay();
    }

    // update the balls / strikes TextViews
    private void updateBatterDisplay() {
        batterTextViews[batterStats.BALLS.ordinal()].setText(String.valueOf(balls));
        batterTextViews[batterStats.STRIKES.ordinal()].setText(String.valueOf(strikes));
    }

    // update the outs TextView
    private void updateOuts() {
        outsLabel.setText(String.valueOf(outs_inThisInning()));
    }

    // set the border color for the current half-inning to red
    private void setBorderColor() {
        scoreBoard_bothTeams[half][currentInning()].setBackgroundResource(R.drawable.rectangle_red);
    }

    // set the border color for the current team/inning's score TextView to gray
    // The logic is that the border changes to gray, then to red on every out
    // When we go to a new half-inning, the previous half-inning's border will be left gray
    private void unsetBorderColor() {
        scoreBoard_bothTeams[half][currentInning()].setBackgroundResource(R.drawable.rectangle_gray);
    }

    // For better code readability
    private int outs_inThisInning() {
        return outs % OUTS_PER_HALF;
    }

    private int currentInning() {
        return outs / OUTS_PER_INNING;
    }

    // see if the game is tied
    private boolean gameTied() {
        return (Integer.parseInt(totalScoreTextViews[teams.TEAMA.ordinal()].getText().toString()) ==
                Integer.parseInt(totalScoreTextViews[teams.TEAMB.ordinal()].getText().toString()));
    }

    // game is over; disable the buttons until new game is created
    private void gameOver() {
        for (Button button : buttons) {
            button.setTextColor(Color.GRAY);
            button.setEnabled(false);
        }
        TextView view = findViewById(R.id.total_label);
        view.setText("Final");
    }
}