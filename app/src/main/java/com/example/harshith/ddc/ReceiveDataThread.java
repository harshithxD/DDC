package com.example.harshith.ddc;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by harshith on 17/6/16.
 */

public class ReceiveDataThread extends Thread {
    int mode;
    private BluetoothSocket bluetoothSocket;
    private InputStream inputStream;
    Handler handler;
    Handler processHandler = null;
    GlobalClass globalClass;
    public int readStatus;
    private StringBuilder stringBuilder = null;
    int[] readings;
    Gesture []a;

    public ReceiveDataThread(BluetoothSocket bluetoothSocket, Handler handler,GlobalClass globalClass,int mode) {
        this.bluetoothSocket = bluetoothSocket;
        this.handler = handler;
        this.globalClass = globalClass;
        
        this.mode = mode;
        a = new Gesture[8];
        for (int i = 0; i<8; i++) {
            a[i] = new StaticGesture();
        }

        int[]hand; int q=0;

        q=0; hand = new int [] {0,0,1,1,0,8,8,8,8,8,8};
        // Music Player
        a[q].updateFrame(hand);

        q=1; hand = new int [] {0,0,1,1,1,8,8,8,8,8,8};
        // Volume
        a[q].updateFrame(hand);

        q=2; hand = new int [] {1,0,1,1,1,8,8,8,8,1,8};
        // OK Google
        a[q].updateFrame(hand);

        q=3; hand = new int [] {0,1,1,1,0,8,8,8,8,1,8};
        // Back
        a[q].updateFrame(hand);

        q=4; hand = new int [] {0,0,0,1,1,8,8,8,-1,8,8};
        // cam open
        a[q].updateFrame(hand);

        q=5; hand = new int [] {0,1,1,1,1,8,8,8,8,1,8};
        // cam click
        a[q].updateFrame(hand);

        q=6; hand = new int [] {1,0,0,1,1,8,8,8,8,1,8};
        // Music Play
        a[q].updateFrame(hand);

        q=7; hand = new int [] {1,0,0,1,0,8,8,8,8,1,8};
        // Home
        a[q].updateFrame(hand);

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

            Live staticLive = new Live();
            int no = 30;
            //StaticLive.print();
            int count = 0;
            int gestActive = -1; boolean stay = true;
            if(mode == Constants.MODE_NORMAL) {
                while (true) {
                    bytes = inputStream.read(buffer);
                    readStatus = Constants.READ_STATUS_OK;
                    String readMessage = new String(buffer, 0, bytes);
                    globalClass.getStringBuilder().append(readMessage);
                    int endOfLineIndex = globalClass.getStringBuilder().indexOf("~");
                    if (endOfLineIndex > 0) {
                        int startOfLineIndex = globalClass.getStringBuilder().indexOf("#");
                        if (startOfLineIndex > endOfLineIndex || startOfLineIndex == -1) {
                            startOfLineIndex = 0;
                        } else if (startOfLineIndex == 0) {
                            startOfLineIndex = 1;
                        }
                        String dataIn = globalClass.getStringBuilder().substring(startOfLineIndex, endOfLineIndex);

                        String[] readingStrings = dataIn.split("\\+");
                        readings = new int[readingStrings.length];
                        for (int i = 0; i != readingStrings.length; i++) {
                            try {
                                readings[i] = Integer.valueOf(readingStrings[i]);
                            } catch (NumberFormatException e) {

                            } catch (NullPointerException e) {

                            }
                        }
                        String testConvert = "";
                        for (int reading : readings) {
                            testConvert += " " + reading;
                        }
                        L.m(testConvert);
                        globalClass.getStringBuilder().delete(0, endOfLineIndex + 2);

                        if (count == no) {
                            Log.d("Gesture", "Gesture " + gestActive + " is activated");
                            mode = Constants.MODE_NORMAL;
                            if (gestActive == 0) {
                                Looper.prepare();
                                handler.obtainMessage(Constants.READ_STATUS, readStatus, mode, Constants.MUSIC_PLAYER).sendToTarget();
                                Looper.loop();
                            } else if (gestActive == 1) {
                                mode = Constants.MODE_VOLUME;
                                Looper.prepare();
                                Message msg = handler.obtainMessage(Constants.READ_STATUS, readStatus, mode, Constants.VOLUME_CONTROL);
                                Bundle bundle = new Bundle();
                                bundle.putInt(Constants.VOLUME,readings[8]);
                                msg.setData(bundle);
                                handler.sendMessage(msg);
                                Looper.loop();
                            } else if (gestActive == 2) {
                                Looper.prepare();
                                handler.obtainMessage(Constants.READ_STATUS, readStatus, mode, Constants.OK_GOOGLE).sendToTarget();
                                Looper.loop();
                            } else if (gestActive == 3) {
                                Looper.prepare();
                                handler.obtainMessage(Constants.READ_STATUS, readStatus, mode, Constants.BACK).sendToTarget();
                                Looper.loop();
                            } else if (gestActive == 4) {
                                Looper.prepare();
                                handler.obtainMessage(Constants.READ_STATUS, readStatus, mode, Constants.OPEN_CAMERA).sendToTarget();
                                Looper.loop();
                            } else if (gestActive == 5) {
                                Looper.prepare();
                                handler.obtainMessage(Constants.READ_STATUS, readStatus, mode, Constants.CAMERA_CLICK).sendToTarget();
                                Looper.loop();
                            } else if (gestActive == 6) {
                                Looper.prepare();
                                handler.obtainMessage(Constants.READ_STATUS, readStatus, mode, Constants.PLAY_PAUSE).sendToTarget();
                                Looper.loop();
                            } else if (gestActive == 7) {
                                Looper.prepare();
                                handler.obtainMessage(Constants.READ_STATUS, readStatus, mode, Constants.HOME).sendToTarget();
                                Looper.loop();
                            }

                            count = 0;
                            gestActive = -1;
                            //if( str.equals("n") ) { stay = false; break; }
                        }

                        if (count < no) {
                            if (readings.length == 11) {
                                staticLive.update(readings);
                            }
                        }

                        for (int i = 0; i < 8; i++) {
                            if (a[i].isInFrame(staticLive)) {
                                if (gestActive == i) {
                                    count++;
                                    break;
                                } else {
                                    gestActive = i;
                                    count = 1;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            else if(mode == Constants.MODE_VOLUME){
                while (true) {
                    bytes = inputStream.read(buffer);
                    readStatus = Constants.READ_STATUS_OK;
                    String readMessage = new String(buffer, 0, bytes);
                    globalClass.getStringBuilder().append(readMessage);
                    int endOfLineIndex = globalClass.getStringBuilder().indexOf("~");
                    if (endOfLineIndex > 0) {
                        int startOfLineIndex = globalClass.getStringBuilder().indexOf("#");
                        if (startOfLineIndex > endOfLineIndex || startOfLineIndex == -1) {
                            startOfLineIndex = 0;
                        } else if (startOfLineIndex == 0) {
                            startOfLineIndex = 1;
                        }
                        String dataIn = globalClass.getStringBuilder().substring(startOfLineIndex, endOfLineIndex);

                        String[] readingStrings = dataIn.split("\\+");
                        readings = new int[readingStrings.length];
                        for (int i = 0; i != readingStrings.length; i++) {
                            try {
                                readings[i] = Integer.valueOf(readingStrings[i]);
                            } catch (NumberFormatException e) {

                            } catch (NullPointerException e) {

                            }
                        }
                        String testConvert = "";
                        for (int reading : readings) {
                            testConvert += " " + reading;
                        }
                        L.m(testConvert);
                        globalClass.getStringBuilder().delete(0, endOfLineIndex + 2);
                        if(readings.length == 11){
                            staticLive.update(readings);
                        }
                        if(!a[1].isInFrame(staticLive)){
                            mode = Constants.MODE_NORMAL;
                            Looper.prepare();
                            handler.obtainMessage(Constants.READ_STATUS,readStatus,mode,Constants.VOLUME_CONTROL).sendToTarget();
                            Looper.loop();
                        }
                        else {
                            mode = Constants.MODE_VOLUME;
                            Looper.prepare();
                            Message msg = handler.obtainMessage(Constants.READ_STATUS, readStatus, mode, Constants.VOLUME_CONTROL);
                            Bundle bundle = new Bundle();
                            bundle.putInt(Constants.VOLUME,readings[8]);
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                            Looper.loop();
                        }
                    }
                }
            }

        }
        catch (IOException e){
            readStatus = Constants.READ_STATUS_NOT_OK;
            Looper.prepare();
            handler.obtainMessage(Constants.READ_STATUS,readStatus,0,null);
            Looper.loop();
        }
    }

    public void convert(){
        int endOfLineIndex = globalClass.getStringBuilder().indexOf("~");
        if (endOfLineIndex > 0) {
            int startOfLineIndex = globalClass.getStringBuilder().indexOf("#");
            if (startOfLineIndex > endOfLineIndex || startOfLineIndex == -1) {
                startOfLineIndex = 0;
            }
            else if (startOfLineIndex == 0) {
                startOfLineIndex = 1;
            }
            String dataIn = globalClass.getStringBuilder().substring(startOfLineIndex, endOfLineIndex);

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
            globalClass.getStringBuilder().delete(0, endOfLineIndex + 2);

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
