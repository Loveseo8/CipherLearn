package com.add.cipherlearn;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Scanner;

public class Learn extends AppCompatActivity {


    SharedPreferences sharedPreferencesCipher;
    String cipher_name;
    String ID = "cipher name";
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

            cipher_name = sharedPreferencesCipher.getString(ID, "");

        }

        documentView = findViewById(R.id.webview);

        getSupportActionBar().setTitle(cipher_name);

        pathReference = storageRef.child("Learn/" + cipher_name + ".html");


        final File localFile;
        try {
            localFile = File.createTempFile(cipher_name, ".html");

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
}