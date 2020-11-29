package com.add.cipherlearn;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Practice extends AppCompatActivity {

    SharedPreferences sharedPreferencesCipher;
    String selectedCipher;
    String ID = "selected_cipher";
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    StorageReference pathReference;
    TextView taskText;
    int current = 0;
    Button next;
    List<String> tasksTexts;
    List<String> tasksAnswers;
    int score = 0;
    String currentAnswer;
    int numberOfTasks = 0;
    EditText answerEditText;
    SharedPreferences sharedPreferencesUserScore;
    String ID_USERSCORE = "user_score";
    SharedPreferences sharedPreferencesNumber;
    String ID_NUMBER = "number";
    TextView copy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        taskText = findViewById(R.id.tasktext);
        next = findViewById(R.id.next);
        answerEditText = findViewById(R.id.edit_text);
        copy = findViewById(R.id.copy);

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("task text", tasksTexts.get(current));
                clipboard.setPrimaryClip(clip);

                Toast toast = Toast.makeText(getApplicationContext(), "Text Copied", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        sharedPreferencesUserScore = getSharedPreferences(ID_USERSCORE, MODE_PRIVATE);
        sharedPreferencesNumber = getSharedPreferences(ID_NUMBER, MODE_PRIVATE);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userAnswer = answerEditText.getText().toString();

                if(userAnswer.isEmpty()){

                    answerEditText.setError("Type in your answer!");
                    answerEditText.requestFocus();

                }else {

                    if(userAnswer.contains(currentAnswer)) score += 1;

                    answerEditText.setText("");

                    if(current == numberOfTasks - 1) {

                        SharedPreferences.Editor editor = sharedPreferencesNumber.edit();
                        editor.putString(ID_NUMBER, String.valueOf(numberOfTasks));
                        editor.commit();

                        SharedPreferences.Editor ed = sharedPreferencesUserScore.edit();
                        ed.putString(ID_USERSCORE, String.valueOf(score));
                        ed.commit();

                        Intent i = new Intent(Practice.this, ResultActivity.class);
                        startActivity(i);


                    } else {
                        current++;

                        taskText.setText(tasksTexts.get(current));
                        currentAnswer = tasksAnswers.get(current).trim();

                    }

                }
            }
        });

        sharedPreferencesCipher = getSharedPreferences(ID, MODE_PRIVATE);
        if (sharedPreferencesCipher.contains(ID)) {

            selectedCipher = sharedPreferencesCipher.getString(ID, "");

            getSupportActionBar().setTitle(selectedCipher);

            pathReference = storageRef.child("Practice/" + selectedCipher + ".xml");


            final File localFile;
            try {
                localFile = File.createTempFile(selectedCipher, ".xml");

                pathReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                        tasksTexts = new ArrayList<>();
                        tasksAnswers = new ArrayList<>();

                        try {
                            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                            Document document = documentBuilder.parse(localFile);

                            Node root = document.getDocumentElement();
                            NodeList tasks = root.getChildNodes();
                            for (int i = 0; i < tasks.getLength(); i++) {
                                Node task = tasks.item(i);
                                if (task.getNodeType() != Node.TEXT_NODE) {
                                    NodeList bookProps = task.getChildNodes();
                                    for(int j = 0; j < bookProps.getLength(); j++) {
                                        Node bookProp = bookProps.item(j);
                                        if (bookProp.getNodeType() != Node.TEXT_NODE) {

                                            if(bookProp.getNodeName().equals("Text")) tasksTexts.add(bookProp.getChildNodes().item(0).getTextContent());
                                            if(bookProp.getNodeName().equals("Answer")) tasksAnswers.add(bookProp.getChildNodes().item(0).getTextContent());
                                        }
                                    }
                                }
                            }

                        } catch (ParserConfigurationException ex) {
                            ex.printStackTrace(System.out);
                        } catch (SAXException ex) {
                            ex.printStackTrace(System.out);
                        } catch (IOException ex) {
                            ex.printStackTrace(System.out);
                        }

                        taskText.setText(tasksTexts.get(current));
                        currentAnswer = tasksAnswers.get(current).trim();
                        if(answerEditText.getText().equals(currentAnswer)) score += 1;
                        numberOfTasks = tasksAnswers.size();
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