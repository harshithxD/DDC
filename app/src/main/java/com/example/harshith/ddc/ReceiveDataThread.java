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

    public void setProcessHandler(Handler processHandler) {
        this.processHandler = processHandler;
    }

    @Override
    public void run(){
        try {
            InputStream tempIn = null;
            tempIn = bluetoothSocket.getInputStream();
            inputStream = tempIn;

            byte[] buffer = new byte[64];
            int bytes = -1;

            bytes = inputStream.read(buffer);
            readStatus = Constants.READ_STATUS_OK;
            String readMessage = new String(buffer,0,bytes);
            stringBuilder.append(readMessage);
            convert();
            ProcessThread processThread = new ProcessThread(globalClass);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
            processThread.start();
            processHandler = globalClass.getProcessHandler();
            while(true) {
                bytes = inputStream.read(buffer);
                readMessage = new String(buffer,0,bytes);
                stringBuilder.append(readMessage);
                convert();
                Looper.prepare();
                if(processHandler != null) {
                    processHandler.obtainMessage(1, readings).sendToTarget();
                }
                else {
                    processHandler = globalClass.getProcessHandler();
                }
                Looper.loop();
            }


        }
        catch (IOException e){
            readStatus = Constants.READ_STATUS_NOT_OK;
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

//            int[] flex = new int[5];
//            for (int i = 0; i != 5; i++) {
//                try {
//                    flex[i] = readings[i];
//                }
//                catch (ArrayIndexOutOfBoundsException e) {
//
//                }
//            }
//
//            int[] mpu = new int[6];
//            for (int i = 0; i != 6; i++) {
//                try {
//                    mpu[i] = readings[5 + i];
//                }
//                catch (ArrayIndexOutOfBoundsException e) {
//
//                }
//            }
        }
    }
}
