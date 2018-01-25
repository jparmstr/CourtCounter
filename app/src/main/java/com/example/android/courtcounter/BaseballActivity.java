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

public class BaseballActivity extends AppCompatActivity {

    private int[] scoreTeamA = new int[10];
    private int[] scoreTeamB = new int[10];

    // Inning. Top of the first is 1.0, bottom of the first is 1.5, and so on.
    private double inning = 1.0;

    private int balls = 0;
    private int strikes = 0;
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

        // Show back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        updateDisplay();
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

    public void inning_minus() {
        if (inning > 1.0) {
            inning -= 0.5;
        }

        updateDisplay();
    }

    public void inning_plus() {
        if (inning < 10.0) {
            inning += 0.5;
        }

        updateDisplay();
    }

    // Batter gets a ball. If there are 4 balls, reset Balls and Strikes for the next batter.
    public void ball(View view) {
        // Do nothing if the game is over
        if (inning >= 10.0) {
            return;
        }

        balls++;

        if (balls >= 4) {
            balls = 0;
            strikes = 0;
        }

        updateDisplay();
    }

    // Batter gets a strike. If there are 3 strikes, reset Balls and Strikes, add an Out.
    public void strike(View view) {
        // Do nothing if the game is over
        if (inning >= 10.0) {
            return;
        }

        strikes++;

        if (strikes >= 3) {
            strikes = 0;
            balls = 0;
            out(view);
            return;
        }

        updateDisplay();
    }

    // Batter hits the ball. Reset Balls and Strikes.
    public void hit(View view) {
        // Do nothing if the game is over
        if (inning >= 10.0) {
            return;
        }

        strikes = 0;
        balls = 0;

        updateDisplay();
    }

    // Player scores a run. Update team score for the current inning.
    public void scoreRun(View view) {
        // Do nothing if the game is over
        if (inning >= 10.0) {
            return;
        }

        if (inning % 1 == 0.0) {
            scoreTeamA[(int) inning]++;
        } else if (inning % 1 == 0.5) {
            scoreTeamB[(int) inning]++;
        }

        updateDisplay();
    }

    // Batter or Runner is out. Go to next (half) inning if there are 3 Outs.
    public void out(View view) {
        // Do nothing if the game is over
        if (inning >= 10.0) {
            return;
        }

        outs++;

        if (outs >= 3) {
            // Go to next (half) inning
            inning += 0.5;

            // Reset values
            balls = 0;
            strikes = 0;
            outs = 0;
        }

        // See if we're at the end of the game
        if (inning >= 10.0) {
            // (not sure what to do about this)
        }

        updateDisplay();
    }

    public void resetGame() {
        scoreTeamA = new int[10];
        scoreTeamB = new int[10];

        inning = 1.0;
        balls = 0;
        strikes = 0;
        outs = 0;

        updateDisplay();
    }

    // Update all TextViews
    public void updateDisplay() {
        // Get references to all TextViews
        TextView textView_team_a_inning_1 = (TextView) findViewById(R.id.baseball_team_a_inning_1);
        TextView textView_team_a_inning_2 = (TextView) findViewById(R.id.baseball_team_a_inning_2);
        TextView textView_team_a_inning_3 = (TextView) findViewById(R.id.baseball_team_a_inning_3);
        TextView textView_team_a_inning_4 = (TextView) findViewById(R.id.baseball_team_a_inning_4);
        TextView textView_team_a_inning_5 = (TextView) findViewById(R.id.baseball_team_a_inning_5);
        TextView textView_team_a_inning_6 = (TextView) findViewById(R.id.baseball_team_a_inning_6);
        TextView textView_team_a_inning_7 = (TextView) findViewById(R.id.baseball_team_a_inning_7);
        TextView textView_team_a_inning_8 = (TextView) findViewById(R.id.baseball_team_a_inning_8);
        TextView textView_team_a_inning_9 = (TextView) findViewById(R.id.baseball_team_a_inning_9);
        TextView textView_team_a_total_score = (TextView) findViewById(R.id.baseball_team_a_total_score);

        TextView textView_team_b_inning_1 = (TextView) findViewById(R.id.baseball_team_b_inning_1);
        TextView textView_team_b_inning_2 = (TextView) findViewById(R.id.baseball_team_b_inning_2);
        TextView textView_team_b_inning_3 = (TextView) findViewById(R.id.baseball_team_b_inning_3);
        TextView textView_team_b_inning_4 = (TextView) findViewById(R.id.baseball_team_b_inning_4);
        TextView textView_team_b_inning_5 = (TextView) findViewById(R.id.baseball_team_b_inning_5);
        TextView textView_team_b_inning_6 = (TextView) findViewById(R.id.baseball_team_b_inning_6);
        TextView textView_team_b_inning_7 = (TextView) findViewById(R.id.baseball_team_b_inning_7);
        TextView textView_team_b_inning_8 = (TextView) findViewById(R.id.baseball_team_b_inning_8);
        TextView textView_team_b_inning_9 = (TextView) findViewById(R.id.baseball_team_b_inning_9);
        TextView textView_team_b_total_score = (TextView) findViewById(R.id.baseball_team_b_total_score);

        TextView textView_balls = (TextView) findViewById(R.id.baseball_balls);
        TextView textView_strikes = (TextView) findViewById(R.id.baseball_strikes);
        TextView textView_outs = (TextView) findViewById(R.id.baseball_outs);

        // Set all inning TextViews to Grey,
        textView_team_a_inning_1.setBackgroundResource(R.drawable.rectangle_gray);
        textView_team_a_inning_2.setBackgroundResource(R.drawable.rectangle_gray);
        textView_team_a_inning_3.setBackgroundResource(R.drawable.rectangle_gray);
        textView_team_a_inning_4.setBackgroundResource(R.drawable.rectangle_gray);
        textView_team_a_inning_5.setBackgroundResource(R.drawable.rectangle_gray);
        textView_team_a_inning_6.setBackgroundResource(R.drawable.rectangle_gray);
        textView_team_a_inning_7.setBackgroundResource(R.drawable.rectangle_gray);
        textView_team_a_inning_8.setBackgroundResource(R.drawable.rectangle_gray);
        textView_team_a_inning_9.setBackgroundResource(R.drawable.rectangle_gray);

        textView_team_b_inning_1.setBackgroundResource(R.drawable.rectangle_gray);
        textView_team_b_inning_2.setBackgroundResource(R.drawable.rectangle_gray);
        textView_team_b_inning_3.setBackgroundResource(R.drawable.rectangle_gray);
        textView_team_b_inning_4.setBackgroundResource(R.drawable.rectangle_gray);
        textView_team_b_inning_5.setBackgroundResource(R.drawable.rectangle_gray);
        textView_team_b_inning_6.setBackgroundResource(R.drawable.rectangle_gray);
        textView_team_b_inning_7.setBackgroundResource(R.drawable.rectangle_gray);
        textView_team_b_inning_8.setBackgroundResource(R.drawable.rectangle_gray);
        textView_team_b_inning_9.setBackgroundResource(R.drawable.rectangle_gray);

        // Highlight current inning in Red
        if (inning == 1.0) {
            textView_team_a_inning_1.setBackgroundResource(R.drawable.rectangle_red);
        } else if (inning == 1.5) {
            textView_team_b_inning_1.setBackgroundResource(R.drawable.rectangle_red);
        } else if (inning == 2.0) {
            textView_team_a_inning_2.setBackgroundResource(R.drawable.rectangle_red);
        } else if (inning == 2.5) {
            textView_team_b_inning_2.setBackgroundResource(R.drawable.rectangle_red);
        } else if (inning == 3.0) {
            textView_team_a_inning_3.setBackgroundResource(R.drawable.rectangle_red);
        } else if (inning == 3.5) {
            textView_team_b_inning_3.setBackgroundResource(R.drawable.rectangle_red);
        } else if (inning == 4.0) {
            textView_team_a_inning_4.setBackgroundResource(R.drawable.rectangle_red);
        } else if (inning == 4.5) {
            textView_team_b_inning_4.setBackgroundResource(R.drawable.rectangle_red);
        } else if (inning == 5.0) {
            textView_team_a_inning_5.setBackgroundResource(R.drawable.rectangle_red);
        } else if (inning == 5.5) {
            textView_team_b_inning_5.setBackgroundResource(R.drawable.rectangle_red);
        } else if (inning == 6.0) {
            textView_team_a_inning_6.setBackgroundResource(R.drawable.rectangle_red);
        } else if (inning == 6.5) {
            textView_team_b_inning_6.setBackgroundResource(R.drawable.rectangle_red);
        } else if (inning == 7.0) {
            textView_team_a_inning_7.setBackgroundResource(R.drawable.rectangle_red);
        } else if (inning == 7.5) {
            textView_team_b_inning_7.setBackgroundResource(R.drawable.rectangle_red);
        } else if (inning == 8.0) {
            textView_team_a_inning_8.setBackgroundResource(R.drawable.rectangle_red);
        } else if (inning == 8.5) {
            textView_team_b_inning_8.setBackgroundResource(R.drawable.rectangle_red);
        } else if (inning == 9.0) {
            textView_team_a_inning_9.setBackgroundResource(R.drawable.rectangle_red);
        } else if (inning == 9.5) {
            textView_team_b_inning_9.setBackgroundResource(R.drawable.rectangle_red);
        } else {
            // Do nothing
        }

        // Update all inning TextViews (scores)
        // Note: Top of the first is 1.0, bottom of the first is 1.5, and so on.
        textView_team_a_inning_1.setText(String.valueOf(scoreTeamA[1]));
        textView_team_a_inning_2.setText(String.valueOf(scoreTeamA[2]));
        textView_team_a_inning_3.setText(String.valueOf(scoreTeamA[3]));
        textView_team_a_inning_4.setText(String.valueOf(scoreTeamA[4]));
        textView_team_a_inning_5.setText(String.valueOf(scoreTeamA[5]));
        textView_team_a_inning_6.setText(String.valueOf(scoreTeamA[6]));
        textView_team_a_inning_7.setText(String.valueOf(scoreTeamA[7]));
        textView_team_a_inning_8.setText(String.valueOf(scoreTeamA[8]));
        textView_team_a_inning_9.setText(String.valueOf(scoreTeamA[9]));

        textView_team_b_inning_1.setText(String.valueOf(scoreTeamB[1]));
        textView_team_b_inning_2.setText(String.valueOf(scoreTeamB[2]));
        textView_team_b_inning_3.setText(String.valueOf(scoreTeamB[3]));
        textView_team_b_inning_4.setText(String.valueOf(scoreTeamB[4]));
        textView_team_b_inning_5.setText(String.valueOf(scoreTeamB[5]));
        textView_team_b_inning_6.setText(String.valueOf(scoreTeamB[6]));
        textView_team_b_inning_7.setText(String.valueOf(scoreTeamB[7]));
        textView_team_b_inning_8.setText(String.valueOf(scoreTeamB[8]));
        textView_team_b_inning_9.setText(String.valueOf(scoreTeamB[9]));

        // Update total score TextViews
        int totalScore_A = 0;
        int totalScore_B = 0;

        for (int i = 1; i < 9; i++) {
            totalScore_A += scoreTeamA[i];
            totalScore_B += scoreTeamB[i];
        }

        textView_team_a_total_score.setText(String.valueOf(totalScore_A));
        textView_team_b_total_score.setText(String.valueOf(totalScore_B));

        // Update Ball, Strike, Out TextViews
        textView_balls.setText(String.valueOf(balls));
        textView_strikes.setText(String.valueOf(strikes));
        textView_outs.setText(String.valueOf(outs));
    }

}
