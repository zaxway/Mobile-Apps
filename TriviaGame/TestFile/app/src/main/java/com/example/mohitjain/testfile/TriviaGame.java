package com.example.mohitjain.testfile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.*;


import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class TriviaGame extends AppCompatActivity {

    public static ArrayList<String> wordList = new ArrayList<String>();
    public static ArrayList<String> definitionList = new ArrayList<String>();
    public static ArrayList<wordObj> objectList = new ArrayList<wordObj>();
    //public static PrintStream out;
    public static PrintStream out2;
    public static int wordCount = 0;
    final Context context = this;
    public static SQLiteDatabase db;
    String query;
    Scanner scan;
    private static final int REQ_CODE_TAKE_PICTURE = 30210; // 1-65535

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia_game);
//        try {
//            out = new PrintStream(openFileOutput("example.txt", MODE_APPEND));
//            Log.i("ReadFile", "WroteToFile");
//        }
//        catch (FileNotFoundException e) {
//            e.printStackTrace();
//            Log.i("FileNotFound", "FileNotFound");
//        }
        try {
            out2 = new PrintStream(openFileOutput("scoreHistory.txt", MODE_APPEND));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        db = this.openOrCreateDatabase("terms", MODE_PRIVATE, null);
        final Switch sw = (Switch) findViewById(R.id.switch1);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.power);
        sw.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(sw.isChecked()) {
                    mp.start();
                }
                else {
                    mp.stop();
                }
            }
        });
        Button profilePic = (Button) findViewById(R.id.profile);
        profilePic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(picIntent, REQ_CODE_TAKE_PICTURE);
            }
        });

        //DatabaseReference startPath = FirebaseDatabase.getInstance().getReference();
        //startPath.child("Name").setValue(LoginActivity.name);
        //DatabaseReference building = startPath.child("Name");
        //building.child("User").setValue(LoginActivity.name);


        DatabaseReference base = FirebaseDatabase.getInstance().getReference();
        DatabaseReference getPic = base.child(LoginActivity.name + "/Picture");

        getPic.addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot data) {
                String bmps = (String) data.child("Picture").getValue();
                Bitmap bmp = StringToBitMap(bmps);
                ImageView img = (ImageView) findViewById(R.id.imageView);
                img.setImageBitmap(bmp);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });




    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQ_CODE_TAKE_PICTURE && resultCode == RESULT_OK) {
            Bitmap bmp = (Bitmap) intent.getExtras().get("data");
            ImageView img = (ImageView) findViewById(R.id.imageView);
            img.setImageBitmap(bmp);
            String toStore = BitMapToString(bmp);
            DatabaseReference picToStore = FirebaseDatabase.getInstance().getReference();
            DatabaseReference tbl = picToStore.child(LoginActivity.name + "/Picture");
            tbl.child("Picture").setValue(toStore);
        }
    }
    public void playButton(View view) {
        Intent x = new Intent(this, playActivity.class);
        startActivity(x);
    }

    public void wordButton(View view) {
//        Intent y = new Intent(this, wordActivity.class);
//        startActivity(y);
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.activity_word, null);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Add a New Word");
        //builder1.setCancelable(true);
        builder1.setView(dialoglayout);
        final EditText wordEdit = (EditText)dialoglayout.findViewById(R.id.editText2);
        final EditText defintionEdit = (EditText)dialoglayout.findViewById(R.id.editText3);

        builder1.setPositiveButton(
                "Add",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Log.i("hello", "addToFile: ");
                        String wordResult = wordEdit.getText().toString();
                        String defintionResult = defintionEdit.getText().toString();


                        // Creates a SQL Database System
                        scan = new Scanner(getResources().openRawResource(R.raw.termtable));
                        query = "";
                        while (scan.hasNextLine()) { // build and execute queries
                            query += scan.nextLine() + "\n";
                            if (query.trim().endsWith(";")) {
                                db.execSQL(query);
                                query = ""; }
                        }
                        db.execSQL("INSERT INTO termtable (term, defintion) "
                                + "VALUES ('" + wordResult + "', '" + defintionResult + "')");
                        // Ends the table creation

                        // Firebase Term Table Creation
                        DatabaseReference fb = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference table = fb.child(LoginActivity.name + "/Terms and Definitions");
                        table.child(wordResult).setValue(defintionResult);
                        // End of Term Table Creation


                        // creates a new word object
                        wordObj a = new wordObj(wordResult, defintionResult);

                        //adds the word to the arrayList
                        wordList.add(wordResult);
                        // adds the definition to the arrayList
                        definitionList.add(defintionResult);
                        // adds the word+Defn object into our list of objects
                        objectList.add(a);
                        // adds the word to the word file
                        //out.println(wordResult);
                        //out.println(defintionResult);
                        wordCount = wordCount + 1;
                        //Log.i("hello", "addToFile: ");

                        // resets the fill ins to blanks
                        wordEdit.setText("");
                        defintionEdit.setText("");
                        wordEdit.requestFocus();
                    }
                });

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void historyButton(View view) {
        Intent z = new Intent(this, historyActivity.class);
        startActivity(z);
    }

    public void signOut(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent getOut = new Intent(this, LoginActivity.class);
        startActivity(getOut);
    }

    public void topTenActivity(View view) {
        Intent mn = new Intent(this, TopScoresActivity.class);
        startActivity(mn);
    }
}
