package com.example.mohitjain.testfile;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class wordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);
    }


    public void addToFile(View view) throws FileNotFoundException{
        EditText wordEdit = (EditText)findViewById(R.id.editText2);
        EditText defintionEdit = (EditText)findViewById(R.id.editText3);
        String wordResult = wordEdit.getText().toString();
        String defintionResult = defintionEdit.getText().toString();

        // creates a new word object
        wordObj a = new wordObj(wordResult, defintionResult);

        //adds the word to the arrayList
        TriviaGame.wordList.add(wordResult);
        // adds the definition to the arrayList
        TriviaGame.definitionList.add(defintionResult);
        // adds the word+Defn object into our list of objects
        TriviaGame.objectList.add(a);
        // adds the word to the word file
        //TriviaGame.out.println(wordResult);
        //TriviaGame.out.println(defintionResult);
        TriviaGame.wordCount = TriviaGame.wordCount + 1;
        Log.i("hello", "addToFile: ");

        // resets the fill ins to blanks
        wordEdit.setText("");
        defintionEdit.setText("");
        wordEdit.requestFocus();
    }


}
