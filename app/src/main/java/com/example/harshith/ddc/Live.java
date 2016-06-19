package com.example.harshith.ddc;

import java.util.Scanner;

/**
 * Created by harshith on 18/6/16.
 */

public class Live {
    int reading[];
    int sensors = 10;

    public Live(){
        reading = new int [sensors];
    }

    public Live(Live staticLive){
//        reading = new int [staticLive.sensors];
//        for (int i=0; i<sensors; i++) {
//            reading[i] = staticLive.reading[i];
//        }
        reading = staticLive.reading.clone();
    }


    public void updateConsole(){
        Scanner scan = new Scanner(System.in).useDelimiter("\\s");
        for (int i=0; i<sensors; i++) {
            reading[i] = scan.nextInt();
        }
    }

    public void update(int[] readings){
        reading = readings.clone();
    }

    public void print(){
        for (int i=0; i<sensors; i++) {
            L.m(reading[i] + " ");
        }
    }
}
