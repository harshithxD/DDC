package com.example.harshith.ddc;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.io.InputStream;

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
    int[] readings;

    public ReceiveDataThread(BluetoothSocket bluetoothSocket, Handler handler,GlobalClass globalClass) {
        this.bluetoothSocket = bluetoothSocket;
        this.handler = handler;
        this.globalClass = globalClass;
        stringBuilder = new StringBuilder();
    }

    @Override
    public void run(){
        try {
            InputStream tempIn = null;
            tempIn = bluetoothSocket.getInputStream();
            inputStream = tempIn;

            int noOfGestures = 2;

            DynamicGesture []a = new DynamicGesture[noOfGestures];
            for (int i = 0; i<noOfGestures; i++) {
                a[i] = new DynamicGesture();
            }

            int [][]array = new int[5][11];
            int []cons = new int[11];
            for (int i=0; i<5; i++) {
                for (int j=0; j<5; j++) {
                    array[i][j] = i*2;
                }
            }
            //cons = new int[] {1,1,1,1,1,0,0,0,0,0,0};
            cons = new int[] {1,1,1,1,1,1,1,1,1,1,1};
            a[0].updateFrame(array,cons);
            a[0].printData();

            int [][]array2 = new int[5][11];
            int []cons2 = new int[11];
            for (int i=0; i<5; i++) {
                for (int j=0; j<11; j=j+2) {
                    array2[i][j] = i*2;
                }
                for (int j=1; j<11; j=j+2) {
                    array2[i][j] = 0;
                }
            }
            cons2 = new int[] {1,1,1,1,1,0,0,0,1,0,0};
            a[1].updateFrame(array2,cons2);
            a[1].printData();

            // initialising the 10 gestures

            DynamicQueue q = new DynamicQueue(a);
            Live dynamiclive = new Live();


            byte[] buffer = new byte[64];
            int bytes = -1;
            String readMessage;
            int i;
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
                    q.processQueue();
                    i = q.proceedExecution();
                    if(i != -1){
//                        handler.obtainMessage(Constants.READ_STATUS,readStatus,i,null).sendToTarget();
                        L.m("Gesture " + i + " is executed");
                    }
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
}
