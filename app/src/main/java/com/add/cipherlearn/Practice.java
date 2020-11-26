package com.add.cipherlearn;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
    TextView tT;
    int i = 0;
    Button next;
    List<String> tasksTexts;
    List<String> tasksAnswers;
    int score = 0;
    String currentAnswer;
    int numberOfTasks = 0;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        tT = findViewById(R.id.tasktext);
        next = findViewById(R.id.next);
        editText = findViewById(R.id.edit_text);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userAnswer = editText.getText().toString();

                if(userAnswer.isEmpty()){

                    editText.setError("Type in your answer!");
                    editText.requestFocus();

                }else {

                    if(editText.getText().equals(currentAnswer)) score++;

                    editText.setText("");

                    if(i == numberOfTasks - 1) {

                        Intent i = new Intent(Practice.this, ResultActivity.class);
                        startActivity(i);


                    } else {
                        i++;

                        tT.setText(tasksTexts.get(i));
                        currentAnswer = tasksAnswers.get(i);

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

                        tT.setText(tasksTexts.get(i));
                        currentAnswer = tasksAnswers.get(i);
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
}