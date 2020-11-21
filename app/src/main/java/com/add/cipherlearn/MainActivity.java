package com.add.cipherlearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageView left;
    ImageView right;
    Button learn;
    Button practice;
    int position = 0;
    String [] ciphers = {"Atbash Cipher", "Polybius Square", "Porta", "Vigenere", "Autokey", "Playfair", "Random", "ROT13", "Caesar Cipher", "Affine Cipher", "Rail-fence"};
    TextView cipher_text;
    SharedPreferences cipher_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        learn = findViewById(R.id.learn);
        practice = findViewById(R.id.practice);
        cipher_text = findViewById(R.id.cipher_name);

        cipher_text.setText(ciphers[position]);

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                position = (position + 1) % ciphers.length;
                cipher_text.setText(ciphers[position]);
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                position = (position - 1);
                if(position < 0) position += ciphers.length;
                else position %= ciphers.length;
                cipher_text.setText(ciphers[position]);
            }
        });

        learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Learn.class);
                startActivity(i);
            }
        });

        practice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Practice.class);
                startActivity(i);
            }
        });
    }
}