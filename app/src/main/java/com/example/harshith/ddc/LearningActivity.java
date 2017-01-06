package com.example.harshith.ddc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class LearningActivity extends AppCompatActivity {

    private static BroadcastReceiver broadcastReceiver;
    private boolean record = false;
    private ArrayList<ArrayList<Integer>> sessionReading = new ArrayList<>();
    private final int sizeOfGesture = 50;
    private final int noOfGestures = 11;
    private Learning learning;
    NumberPicker np;


    public static BroadcastReceiver getBroadcastReceiver() {
        return broadcastReceiver;
    }

    public static void setBroadcastReceiver(BroadcastReceiver broadcastReceiver) {
        LearningActivity.broadcastReceiver = broadcastReceiver;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);

        final Button startButton = (Button) findViewById(R.id.start_stop_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                record = !record;
                if(record){
                    startButton.setText("Stop  Training");
                }else{
                    startButton.setText("Start Training");
                    flushInstanceReading();
                }
            }
        });

        setBroadcastReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(record){
                    ArrayList<Integer> liveReading = intent.getIntegerArrayListExtra("com.example.harshith.ddc.READING");
                    sessionReading.add(liveReading);
                }
            }
        });

        np = (NumberPicker) findViewById(R.id.gesture_picker);
        np.setMinValue(1);
        np.setMaxValue(noOfGestures);

    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);

        String learnerString = preferences.getString("com.example.harshith.ddc.LEARNER", null);
        if(learnerString == null){
            learning = new Learning(noOfGestures,sizeOfGesture);
        }else {
            Gson gson = new Gson();
            learning = gson.fromJson(learnerString, Learning.class);
        }
    }

    public void flushInstanceReading(){
        // selecting #sizeOfGesture random no of readings
        if (sessionReading.size() > sizeOfGesture){
            Random random = new Random(65536);
            while (sessionReading.size() > sizeOfGesture){
                sessionReading.remove(random.nextInt(sessionReading.size()));
            }

            ArrayList<ArrayList<ArrayList<Integer>>> gestInstanceList = new ArrayList<>();
            gestInstanceList.add(sessionReading);

            HashMap<Integer, ArrayList<ArrayList<ArrayList<Integer>>>> sessionReadings = new HashMap<>();
            sessionReadings.put(np.getValue(), gestInstanceList);
            learning.train(sessionReadings);
        }else {
            Toast.makeText(getApplicationContext(),"Not enough readings",Toast.LENGTH_SHORT);
        }
    }

    public void start() {
//        ReceiveDataThread thread = new ReceiveDataThread();
    }

    public void learnerDiskSave(){
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        preferences.edit();

        SharedPreferences.Editor preferencesEditor = preferences.edit();

        Gson gson = new Gson();
        String learningAsString = gson.toJson(learning);

        preferencesEditor.putString("com.example.harshith.ddc.LEARNER",learningAsString);

        preferencesEditor.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();

        learnerDiskSave();
    }
}
