package com.add.cipherlearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    SharedPreferences sharedPreferencesUserScore;
    String ID_USERSCORE = "user_score";
    SharedPreferences sharedPreferencesNumber;
    String ID_NUMBER = "number";
    Button finish;
    TextView score;
    String userScore;
    String numberOfTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        finish = findViewById(R.id.finish);
        score = findViewById(R.id.userscore);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(i);

            }
        });

        sharedPreferencesUserScore = getSharedPreferences(ID_USERSCORE, MODE_PRIVATE);
        if (sharedPreferencesUserScore.contains(ID_USERSCORE)) userScore = sharedPreferencesUserScore.getString(ID_USERSCORE, "");

        sharedPreferencesNumber = getSharedPreferences(ID_NUMBER, MODE_PRIVATE);
        if (sharedPreferencesNumber.contains(ID_NUMBER)) numberOfTasks = sharedPreferencesNumber.getString(ID_NUMBER, "");

        score.setText(userScore + " out of " + numberOfTasks);

    }
}