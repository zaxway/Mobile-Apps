package com.example.mohitjain.testfile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TopScoresActivity extends AppCompatActivity {
    private static ArrayList<String> listOfTopScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_scores);
        //onResume();
    }

    protected void onResume(Bundle savedInstanceState) {
        listOfTopScores = new ArrayList<String>();
        DatabaseReference s1 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference s2 = s1.child(LoginActivity.name + "/HighestScore");
        s2.addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot data) {
                Long score = (Long) data.getValue();
                String x = score.toString();
                String line = LoginActivity.name + "   " + x + "%";
                listOfTopScores.add(0, line);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });

        ListView lv3 = (ListView) findViewById(R.id.topScores);
        ArrayAdapter adt = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listOfTopScores);
        lv3.setAdapter(adt);
    }
}
