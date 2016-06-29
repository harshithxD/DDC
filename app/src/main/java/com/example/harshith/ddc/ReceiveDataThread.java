package com.example.harshith.ddc;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LoggingPermission;

/**
 * Created by harshith on 17/6/16.
 */

public class ReceiveDataThread extends Thread {
    private BluetoothSocket bluetoothSocket;
    private InputStream inputStream;
    Handler handler;
    Handler processHandler = null;
    GlobalClass globalClass;
    public int readStatus;
    private StringBuilder stringBuilder = null;
    boolean isFirstTime;
    int[] readings;
    int[] threshold;
    public ReceiveDataThread(BluetoothSocket bluetoothSocket, Handler handler,GlobalClass globalClass,boolean b) {
        this.bluetoothSocket = bluetoothSocket;
        this.handler = handler;
        this.globalClass = globalClass;
        stringBuilder = new StringBuilder();
        isFirstTime = b;
    }

    @Override
    public void run(){
        try {
            InputStream tempIn = null;
            tempIn = bluetoothSocket.getInputStream();
            inputStream = tempIn;
            if(!isFirstTime) {
                byte[] clearing = new byte[1000];
                inputStream.read(clearing);
            }

            int noOfGestures = 12;
            int indx; int [][]array;
            int []cons;
            DynamicGesture []a = new DynamicGesture[noOfGestures];
            threshold = new int[noOfGestures];
            // initialising the 11 gestures

            indx=0;
            array = readingsGenerate(new int[] {14,13,18,15,12},new int[] {76,84,88,81,82});
            a[indx] = new DynamicGesture(array.length);

            cons = new int[] {1,1,1,1,1,0,0,0,0,1,0};
            a[indx].updateFrame(array,cons);
            threshold[indx] = 40;

            indx=1;

            array = readingsGenerate(new int[]{2},new int[]{3});
            a[indx] = new DynamicGesture(array.length);

            cons = new int[] {1,1,1,1,1,1,0,0,1,1,0};
            a[indx].updateFrame(array,cons);
//            a[indx].printData();
            threshold[indx] = 40;

            indx=2;
            array = readingsGenerate(new int [] {14,13,18,15,12},new int [] {23,75,90,13,8});

            a[indx] = new DynamicGesture(array.length);

            cons = new int[] {1,1,1,1,1,0,0,0,1,1,0};
            a[indx].updateFrame(array,cons);
//            a[indx].printData();
            threshold[indx] = 40;

            indx=3;
            array = readingsGenerate(new int [] {14,13,18,15,12},new int [] {2,11,2,10,1});

            a[indx] = new DynamicGesture(array.length);

            cons = new int[] {1,1,1,1,1,0,0,0,0,1,0};
            a[indx].updateFrame(array,cons);
//            a[indx].printData();
            threshold[indx] = 40;

            indx=4;
            array = readingsGenerate(new int[]{2},new int[]{3});

            a[indx] = new DynamicGesture(array.length);

            cons = new int[] {1,1,1,1,1,0,0,0,0,1,0};
            a[indx].updateFrame(array,cons);
//            a[indx].printData();
            threshold[indx] = 40;


            indx=5;
            array = readingsGenerate(new int [] {14,13,18,15,12}	,new int [] {47,5,5,5,93});

            a[indx] = new DynamicGesture(array.length);

            cons = new int[] {1,1,1,1,1,0,0,0,1,1,0};
            a[indx].updateFrame(array,cons);
//            a[indx].printData();
            threshold[indx] = 40;


            indx=6;
            array = readingsGenerate(new int[]{2},new int[]{3});


            a[indx] = new DynamicGesture(array.length);

            cons = new int[] {1,1,1,1,1,1,0,0,1,1,0};
            a[indx].updateFrame(array,cons);
//            a[indx].printData();
            threshold[indx] = 40;


            indx=7;
            array = readingsGenerate(new int[]{2},new int[]{3});

            a[indx] = new DynamicGesture(array.length);

            cons = new int[] {1,1,1,1,1,1,0,0,1,1,0};
            a[indx].updateFrame(array,cons);
//            a[indx].printData();
            threshold[indx] = 40;


            indx=8;
            array = readingsGenerate(new int [] {14,13,18,15,12},new int [] {47,81,21,15,8});

            a[indx] = new DynamicGesture(array.length);

            cons = new int[] {1,1,1,1,1,0,0,0,0,1,0};
            a[indx].updateFrame(array,cons);
//            a[indx].printData();
            threshold[indx] = 40;


            indx=9;
            array = readingsGenerate(new int [] {76,84,88,81,82},new int [] {14,5,80,60,75});

            a[indx] = new DynamicGesture(array.length);

            cons = new int[] {1,1,1,1,1,0,0,0,0,1,0};
            a[indx].updateFrame(array,cons);
//            a[indx].printData();
            threshold[indx] = 40;


            indx=10;
            array = readingsGenerate(new int [] {14,5,80,60,75},new int [] {28,77,77,76,94});

            a[indx] = new DynamicGesture(array.length);
            cons = new int[] {1,1,1,1,1,0,0,0,0,1,0};
            a[indx].updateFrame(array,cons);
//            a[indx].printData();
            threshold[indx] = 40;


            indx=11;
            array = readingsGenerate(new int [] {14,5,80,60,75},new int [] {76,84,88,81,82});

            a[indx] = new DynamicGesture(array.length);

            cons = new int[] {1,1,1,1,1,0,0,0,0,1,0};
            a[indx].updateFrame(array,cons);
//            a[indx].printData();
            threshold[indx] = 40;

            threshold = new int[]{0,0,0,0,0,0,0,0,0,0,0,0};
            DynamicQueue q = new DynamicQueue(a,threshold);
            Live dynamiclive = new Live();


            byte[] buffer = new byte[64];
            int bytes = -1;
            String readMessage;
            int i, counter = 1;
            convert();
            while(true) {
                bytes = inputStream.read(buffer);
                readStatus = Constants.READ_STATUS_OK;
                readMessage = new String(buffer,0,bytes);
                stringBuilder.append(readMessage);
                convert();
                if(readings != null && readings.length == 11) {
                    dynamiclive.update(readings);
                    q.updateQueue(dynamiclive);
                    if(counter > 20) {
                        q.processQueue();
                        i = q.proceedExecution();
                        if (i != -1) {
                            Looper.prepare();
                            handler.sendMessageDelayed(handler.obtainMessage(Constants.READ_STATUS, readStatus, i, null), 10000);
                            L.m("Gesture " + i + " is executed");
                            q.gestureStatusPrint();
                            Looper.loop();
                        }
                    }
                    counter++;
//                    q.print();
                }
            }
        }
        catch (IOException e){
            readStatus = Constants.READ_STATUS_NOT_OK;
            Looper.prepare();
            handler.obtainMessage(Constants.READ_STATUS,readStatus,0,null).sendToTarget();
            Looper.loop();
        }
    }

    public void convert(){
        int endOfLineIndex = stringBuilder.indexOf("~");
        if (endOfLineIndex > 0) {
            int startOfLineIndex = stringBuilder.indexOf("#");
            if (startOfLineIndex > endOfLineIndex || startOfLineIndex == -1) {
                startOfLineIndex = 0;
            }
            else if (startOfLineIndex == 0) {
                startOfLineIndex = 1;
            }
            String dataIn = stringBuilder.substring(startOfLineIndex, endOfLineIndex);
            String[] readingStrings = dataIn.split("\\+");
            readings = new int[readingStrings.length];
            for (int i = 0; i != readingStrings.length; i++) {
                try {
                    readings[i] = Integer.valueOf(readingStrings[i]);
                } catch (NumberFormatException e) {

                }
                catch (NullPointerException e) {

                }
            }
            String testConvert = "";
            for (int reading : readings) {
                testConvert += " " + reading;
            }
            L.m(testConvert);
            stringBuilder.delete(0, endOfLineIndex + 2);
        }
    }

    public void lineDraw(int [][]array, int start, int value, int count, int sensor, boolean incr){

        if (incr) {
            for (int i=0; i<count; i++) {
                array[start + i][sensor] = value + i;
            }
        }
        else{
            for (int i=0; i<count; i++) {
                array[start + i][sensor] = value - i;
            }
        }
    }

    public void setValue(int [][]array, int value, int sensor, int length){
        for (int i=0; i<length; i++) {
            array[i][sensor] = value;
        }
    }

    public static int[][] readingsGenerate(int[] low,int[] high){
        int size=0;
        for(int i=0;i<5;i++){
            int diff=high[i]-low[i];
            if(diff>size){
                size=diff;
            }
        }
        //now size = maximum difference between low and high values
        int [] [] array=new int[size][11];
        float size2=size-1;
        for(int i=0;i<size;i++){
            array[i][0]=low[0]+(int)((high[0]-low[0])*(i/size2));
            array[i][1]=low[1]+(int)((high[1]-low[1])*(i/size2));
            array[i][2]=low[2]+(int)((high[2]-low[2])*(i/size2));
            array[i][3]=low[3]+(int)((high[3]-low[3])*(i/size2));
            array[i][4]=low[4]+(int)((high[4]-low[4])*(i/size2));

            array[i][5]=0;
            array[i][6]=0;
            array[i][7]=0;
            array[i][8]=0;
            array[i][9]=0;
            array[i][10]=0;
        }
        return array;
    }
}
