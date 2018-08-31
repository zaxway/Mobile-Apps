package com.example.mohitjain.testfile;

import android.content.Context;
import android.database.Cursor;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class playActivity extends AppCompatActivity {
    String word = "";
    String words = "";
    int randomInteger = 0;
    String definition = "";
    String definitions = "";
    double correctCounter = 0.0;
    ArrayAdapter<String> listAdapt;
    int wordCounter = 0;
    Random rand = new Random();
    int w, x, y, z;
    String randDef1, randDef2, randDef3, randDef4;
    wordObj obj5, obj4, obj3, obj2, obj1, newObj;
    ArrayList<String> fiveDefn = new ArrayList<String>();
    ListView lv;
    TextView tv;
    TextToSpeech t1;
    Switch sw;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        lv = (ListView) findViewById(R.id.list1);
        tv = findViewById(R.id.textView);
        listAdapt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fiveDefn);
        t1 = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                // when done loading run this...
                if(i != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });

        sw = (Switch) findViewById(R.id.ttsSwitch);

        // Let's add word objects into the object list from our database
        Cursor cr = TriviaGame.db.rawQuery(
                "SELECT term, defintion FROM termtable", null);
        if (cr.moveToFirst()) {
            do {
                words = cr.getString(cr.getColumnIndex("term"));
                definitions = cr.getString(cr.getColumnIndex("defintion"));
                newObj = new wordObj(words, definitions);
                TriviaGame.objectList.add(newObj);
                Log.i("added Word", "added Definition");
            } while (cr.moveToNext());
            cr.close();
        }


        onStart(savedInstanceState);
    }

    public void getWordAndRandomDefinitions() {
        // want to load the words one by one from our word array list
        // get a random() integer i, random should be between 0 and wordlist.length().
        randomInteger = rand.nextInt(TriviaGame.objectList.size()) + 0;
        word = TriviaGame.objectList.get(randomInteger).getWord();
        obj5 = TriviaGame.objectList.get(randomInteger);
        // show word on text
        tv.setText(word);

        // creates a string arrayList to store the five definitions
        // want to get the word definition first
        definition = TriviaGame.objectList.get(randomInteger).getDefintion(); // this is the correct definition for our word
        fiveDefn.add(definition);
        TriviaGame.objectList.remove(obj5);
        // these variables are going to be random variables used to get a random definition

        w = rand.nextInt(TriviaGame.objectList.size()) + 0;
        randDef1 = TriviaGame.objectList.get(w).getDefintion();
        fiveDefn.add(randDef1);
        // remove w from arrayList
        obj4 = TriviaGame.objectList.get(w);
        TriviaGame.objectList.remove(obj4);

        x = rand.nextInt(TriviaGame.objectList.size()) + 0;
        randDef2 = TriviaGame.objectList.get(x).getDefintion();
        fiveDefn.add(randDef2);
        // remove x from arrayList
        obj3 = TriviaGame.objectList.get(x);
        TriviaGame.objectList.remove(obj3);

        y = rand.nextInt(TriviaGame.objectList.size()) + 0;
        randDef3 = TriviaGame.objectList.get(y).getDefintion();
        fiveDefn.add(randDef3);
        // remove y from arrayList
        obj2 = TriviaGame.objectList.get(y);
        TriviaGame.objectList.remove(obj2);

        z = rand.nextInt(TriviaGame.objectList.size()) + 0;
        randDef4 = TriviaGame.objectList.get(z).getDefintion();
        fiveDefn.add(randDef4);
        // remove z from arrayList
        obj1 = TriviaGame.objectList.get(z);
        TriviaGame.objectList.remove(obj1);

        // then add all 4 removals back. The removals are used to avoid redundancy.
        TriviaGame.objectList.add(obj1);
        TriviaGame.objectList.add(obj2);
        TriviaGame.objectList.add(obj3);
        TriviaGame.objectList.add(obj4);
        TriviaGame.objectList.add(obj5);

    }

    protected void onStart(Bundle savedInstanceState) {
        // show definitions in an ordered list of buttons.
        // arrayList array adapter in order to populate list.
        getWordAndRandomDefinitions();
        sw.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(!(sw.isChecked())) {
                    t1.stop();
                }
                else {
                    t1.speak(word, TextToSpeech.QUEUE_ADD, null);
                    t1.speak(definition, TextToSpeech.QUEUE_ADD, null);
                    t1.speak(randDef1, TextToSpeech.QUEUE_ADD, null);
                    t1.speak(randDef2, TextToSpeech.QUEUE_ADD, null);
                    t1.speak(randDef3, TextToSpeech.QUEUE_ADD, null);
                    t1.speak(randDef4, TextToSpeech.QUEUE_ADD, null);
                }
            }
        });

        lv.setAdapter(listAdapt);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String userInput = adapterView.getAdapter().getItem(i).toString();
                // check if userClicked input == definition
                if (userInput.equals(definition)) {
                    Context context = getApplicationContext();
                    CharSequence text = "Correct!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    correctCounter = correctCounter + 1;
                    wordCounter = wordCounter + 1;

                } else {
                    Context context = getApplicationContext();
                    CharSequence text = "Not Correct!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    wordCounter = wordCounter + 1;
                }
                if (wordCounter == 5) {
                    String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    TriviaGame.out2.println(time);
                    TriviaGame.out2.println((correctCounter/5.0) * 100 + "");
                    LoginActivity.currentScore = (correctCounter/5.0) * 100;
                    if (LoginActivity.currentScore > LoginActivity.highestScore) {
                        LoginActivity.highestScore = LoginActivity.currentScore;
                        DatabaseReference highScore = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference see = highScore.child(LoginActivity.name + "/HighestScore");
                        see.setValue(LoginActivity.highestScore);
                    }
                    finish();
                }
                else {
                    fiveDefn.clear();
                    getWordAndRandomDefinitions();
                    listAdapt.notifyDataSetChanged();
                }

            }
        });
    }
}
