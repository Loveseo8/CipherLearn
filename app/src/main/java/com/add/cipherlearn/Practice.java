package com.add.cipherlearn;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Practice extends AppCompatActivity {

    SharedPreferences sharedPreferencesCipher;
    String cipher_name;
    String ID = "cipher name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        sharedPreferencesCipher = getSharedPreferences(ID, MODE_PRIVATE);
        if(sharedPreferencesCipher.contains(ID)){

            cipher_name = sharedPreferencesCipher.getString(ID, "");

        }

        getSupportActionBar().setTitle(cipher_name);

    }
}