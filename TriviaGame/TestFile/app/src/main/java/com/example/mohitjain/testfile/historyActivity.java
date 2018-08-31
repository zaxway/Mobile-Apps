package com.example.mohitjain.testfile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Scanner;

public class historyActivity extends AppCompatActivity {
    private static ArrayList<String> scoreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        scoreList = new ArrayList<String>();
        try {
            Scanner console = new Scanner(openFileInput("scoreHistory.txt"));
            String time = "";
            String score = "";
            while (console.hasNextLine()) {
                time = console.nextLine();
                System.out.println(time);
                score = console.nextLine();
                String line = time + "   " + score + "%";
                scoreList.add(0, line);

            }
        }
        catch (Exception e) {
            System.out.println("Error");
            Log.i("Error", "addToFile: ");
        }
        ListView lv2 = (ListView) findViewById(R.id.list2);
        ArrayAdapter adt = new ArrayAdapter(this, android.R.layout.simple_list_item_1, scoreList);
        lv2.setAdapter(adt);

    }


}
