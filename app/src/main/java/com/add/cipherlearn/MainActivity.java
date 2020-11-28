package com.add.cipherlearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    ImageView previousCipher;
    ImageView nextCipher;
    Button learn;
    Button practice;
    int position = 0;
    String [] ciphers = {"Atbash Cipher", "Baconian Cipher", "Caesar Cipher", "ROT13 Cipher", "Rail-fence Cipher", "Base64 Cipher", "Simple Substitution Cipher", "Columnar Transposition Cipher",
    "Vigenère Cipher", "Bifid Cipher"};
    TextView currentCipher;
    SharedPreferences sharedPreferencesCipher;
    String ID = "selected_cipher";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isConnected()) {

            sharedPreferencesCipher = getSharedPreferences(ID, MODE_PRIVATE);

            previousCipher = findViewById(R.id.left);
            nextCipher = findViewById(R.id.right);
            learn = findViewById(R.id.learn);
            practice = findViewById(R.id.practice);
            currentCipher = findViewById(R.id.cipher_name);

            currentCipher.setText(ciphers[position]);

            previousCipher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    position = (position + 1) % ciphers.length;
                    currentCipher.setText(ciphers[position]);
                }
            });

            nextCipher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    position = (position - 1);
                    if (position < 0) position += ciphers.length;
                    else position %= ciphers.length;
                    currentCipher.setText(ciphers[position]);
                }
            });

            learn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SharedPreferences.Editor editor = sharedPreferencesCipher.edit();
                    editor.putString(ID, ciphers[position]);
                    editor.commit();

                    Intent i = new Intent(MainActivity.this, Learn.class);
                    startActivity(i);
                }
            });

            practice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SharedPreferences.Editor editor = sharedPreferencesCipher.edit();
                    editor.putString(ID, ciphers[position]);
                    editor.commit();

                    Intent i = new Intent(MainActivity.this, Practice.class);
                    i.putExtra("cipher name", currentCipher.getText().toString());
                    startActivity(i);
                }
            });
        } else {

            setContentView(R.layout.no_internet_connection);

        }
    }

        @Override
        public void onBackPressed () {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Exit");
            builder.setCancelable(false);
            builder.setMessage("Are you sure you want to quit?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {


                @Override
                public void onClick(DialogInterface dialog, int which) {

                    finishAffinity();

                }
            });
            builder.setNeutralButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.cancel();

                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
    }

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }
}