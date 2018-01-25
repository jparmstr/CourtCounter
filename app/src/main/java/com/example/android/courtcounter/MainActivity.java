package com.example.android.courtcounter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Score Keeper");
    }

    public void goToBasketballActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), BasketballActivity.class);
        startActivity(intent);
    }

    public void goToBaseballActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), BaseballActivity.class);
        startActivity(intent);
    }

    public void goToFootballActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), FootballActivity.class);
        startActivity(intent);
    }

    public void goToSoccerActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), SoccerActivity.class);
        startActivity(intent);
    }

}
