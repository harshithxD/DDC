package com.example.harshith.ddc;

import android.Manifest;
import android.app.Service;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import java.util.UUID;

import static android.content.ContentValues.TAG;

/**
 * Created by harshith on 17/6/16.
 */

public class ReceiveService extends Service {
    ConnectThread connectThread;
    ReceiveDataThread receiveDataThread;
    BluetoothSocket bluetoothSocket;
    private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public Handler handler;
    GlobalClass globalClass;
    @Override
    public void onCreate() {

        handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                if(message.what == Constants.CONNECTION_STATUS) {
                    if(message.arg1 == Constants.CONNECTION_STATUS_OK){
                        Toast.makeText(getApplicationContext(),"Connected",Toast.LENGTH_LONG).show();
                        bluetoothSocket = (BluetoothSocket) message.obj;
                        receiveDataThread = new ReceiveDataThread((BluetoothSocket) message.obj,handler,globalClass);
                        receiveDataThread.start();
                    }
                    else if((int) message.obj == Constants.CONNECTION_STATUS_NOT_CONNECTED){
                        Toast.makeText(getApplicationContext(),"Couldn't Connect to Dextera Domini, Check whether it is switched on",Toast.LENGTH_SHORT).show();
                    }
                }
                else if (message.what == Constants.READ_STATUS) {
                    if(((String)message.obj).equals(Constants.OPEN_CAMERA)){
                        openCamera();
                        resumeReading();
                    }
                    else if(message.obj.equals(Constants.OK_GOOGLE)){
                        okGoogle();
                        L.s(getBaseContext(),"Ok Google");
                        resumeReading();
                    }
                    else if (message.obj.equals(Constants.GOOGLE_NOW)){
                        googleNow();
                        resumeReading();
                    }
                    else if(message.obj.equals(Constants.PLAY_PAUSE)){
                        audioPlayPause();
                        resumeReading();
                    }
                }
            }
        };
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        globalClass = (GlobalClass) getApplication();
        if(intent != null) {
            String address = intent.getStringExtra(MainActivity.EXTRA_DEVICE_ADDRESS);

            connectThread = new ConnectThread(address, uuid, handler,globalClass) {

            };
            connectThread.start();
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy(){

    }

    public void openCamera(){
        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            PackageManager pm = getBaseContext().getPackageManager();

            final ResolveInfo mInfo = pm.resolveActivity(i, 0);

            Intent intent = new Intent();
            intent.setComponent(new ComponentName(mInfo.activityInfo.packageName, mInfo.activityInfo.name));
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e){ Log.i(TAG, "Unable to launch camera: " + e); }
    }

    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void resumeReading(){
        receiveDataThread = new ReceiveDataThread(bluetoothSocket,handler,globalClass);
        receiveDataThread.start();
    }

    public void googleNow(){
        Intent intent = new Intent(Intent.ACTION_ASSIST);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void okGoogle(){
        Intent intent = new Intent(RecognizerIntent.ACTION_VOICE_SEARCH_HANDS_FREE);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void audioPlayPause(){
        AudioManager audioManager = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
        KeyEvent downEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
        audioManager.dispatchMediaKeyEvent(downEvent);
        KeyEvent upEvent = new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
        audioManager.dispatchMediaKeyEvent(upEvent);
    }
}

