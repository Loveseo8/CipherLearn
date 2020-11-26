package com.add.cipherlearn;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import java.io.File;
import java.io.IOException;

public class Learn extends AppCompatActivity {

    SharedPreferences sharedPreferencesCipher;
    String selected_cipher;
    String ID = "selected_cipher";
    WebView documentView;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    StorageReference pathReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        sharedPreferencesCipher = getSharedPreferences(ID, MODE_PRIVATE);
        if(sharedPreferencesCipher.contains(ID)){

            selected_cipher = sharedPreferencesCipher.getString(ID, "");

        }

        documentView = findViewById(R.id.webview);

        getSupportActionBar().setTitle(selected_cipher);

        pathReference = storageRef.child("Learn/" + selected_cipher + ".html");


        final File localFile;
        try {
            localFile = File.createTempFile(selected_cipher, ".html");

            pathReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    documentView.getSettings().setJavaScriptEnabled(true);
                    documentView.getSettings().setAllowFileAccess(true);
                    documentView.getSettings().setAllowContentAccess(true);
                    documentView.getSettings().setAppCacheEnabled(true);
                    documentView.getSettings().setDomStorageEnabled(true);
                    documentView.getSettings().setAllowFileAccessFromFileURLs(true);
                    documentView.getSettings().setAllowUniversalAccessFromFileURLs(true);


                    documentView.loadUrl("file:///" + localFile.getPath());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {

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
}