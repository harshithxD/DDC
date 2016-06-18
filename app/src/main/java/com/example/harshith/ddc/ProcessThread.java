package com.example.harshith.ddc;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;


/**
 * Created by harshith on 18/6/16.
 */

public class ProcessThread extends Thread{
    int[] liveReadings;
    Handler handler;
    GlobalClass globalClass;

    public ProcessThread(GlobalClass globalClass) {
        liveReadings = null;
        this.globalClass = globalClass;

    }
    @Override
    public void run() {
        Looper.prepare();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                liveReadings = (int[]) msg.obj;
                L.m(liveReadings.toString());
            }
        };
        globalClass.setProcessHandler(handler);
        Looper.loop();
        while (true){
            if(liveReadings != null){

            }
        }
    }
}
