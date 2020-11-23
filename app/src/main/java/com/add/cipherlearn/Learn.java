package com.add.cipherlearn;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

        getSupportActionBar().setTitle(cipher_name);

        pathReference = storageRef.child("Learn/" + cipher_name + ".html");

        final File localFile;
        try {
            localFile = new File(getCacheDir(), cipher_name + ".html");
            if (!localFile.exists() || localFile.length() == 0){
                localFile.createNewFile();
                pathReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.d("TAGA", "onSuccess");
                        try {
                            Scanner sc = new Scanner(localFile);

                            while(sc.hasNextLine()){
                                Log.d("TAGA", sc.nextLine());
                            }

                            documentView = findViewById(R.id.webview);
                            documentView.loadUrl(localFile.getCanonicalPath());

                        } catch (FileNotFoundException e) {
                            Log.d("TAGA", "FileNotFoundException");
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Log.d("TAGA", "onFailure: " + exception.getMessage());
                    }
                }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.d("TAGA", "onProgress");
                        // taskSnapshot.getBytesTransferred()
                        // taskSnapshot.getTotalByteCount();
                    }
                });
            }else{
                Log.d("TAGA", "fromCacheDir" + "   " + localFile.toURI());
                Scanner sc = new Scanner(localFile);
                while(sc.hasNextLine()){
                    Log.d("TAGA", sc.nextLine());
                }

                documentView = findViewById(R.id.webview);
                documentView.loadUrl("file:///" + getCacheDir().getAbsolutePath() + "/" + cipher_name + ".html");
            }
//            localFile = File.createTempFile(cipher_name, "html");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}