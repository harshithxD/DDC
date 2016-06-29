package com.example.harshith.ddc;

import android.app.Instrumentation;
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
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import java.util.UUID;

import static android.content.ContentValues.TAG;

/**
 * Created by harshith on 17/6/16.
 */

public class ReceiveService extends Service {
    int mode = 0;
    ConnectThread connectThread;
    ReceiveDataThread receiveDataThread;
    private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public Handler handler;
    GlobalClass globalClass;
    BluetoothSocket bluetoothSocket;
    @Override
    public void onCreate() {

        handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                if(message.what == Constants.CONNECTION_STATUS) {
                    if(message.arg1 == Constants.CONNECTION_STATUS_OK){
                        Toast.makeText(getApplicationContext(),"Connected",Toast.LENGTH_LONG).show();
                        bluetoothSocket = (BluetoothSocket) message.obj;
                        receiveDataThread = new ReceiveDataThread((BluetoothSocket) message.obj,handler,globalClass,true);
                        receiveDataThread.start();
                    }
                    else if(message.arg1 == Constants.CONNECTION_STATUS_NOT_CONNECTED){
                        Toast.makeText(getApplicationContext(),"Couldn't Connect to Dextera Domini, Check whether it is switched on",Toast.LENGTH_SHORT).show();
                    }
                }
                else if (message.what == Constants.READ_STATUS) {
                    if(message.arg1 == Constants.READ_STATUS_OK){
                        Toast.makeText(getBaseContext(),"Gesture " + message.arg2 + " is executed",Toast.LENGTH_SHORT).show();
                        switch(message.arg2) {
                            case 0:

                                break;
                            case 1:
                                break;
                            case 2:
                                break;
                            case 3:

                                break;
                            case 4:
                                openMusicPlayer();
                                break;
                            case 5:
                                audioPlayPause();
                                break;
                            case 6:

                                break;
                            case 7:
                                break;
                            case 8:
                                okGoogle();
                                break;
                            case 9:
                                openCamera();
                                break;
                            case 10:

                                break;
                            case 11:
                                break;
                            default:
                                break;
                        }

                        resumeReading();
                    }
                    else if(message.arg1 == Constants.READ_STATUS_NOT_OK){
                        L.s(getBaseContext(),"Connection Lost");
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
        receiveDataThread = new ReceiveDataThread(bluetoothSocket,handler,globalClass,false);
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

    public void CameraClick(){
        Thread t= new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_CAMERA);
                }
                catch (Exception e){

                }
            }
        });
        t.start();
    }
    public void VolumeUp(){
        Thread t= new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_VOLUME_UP);
                }
                catch (Exception e){

                }
            }
        });
        t.start();
    }
    public void VolumeDown(){
        Thread t= new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_VOLUME_DOWN);
                }
                catch (Exception e){

                }
            }
        });
        t.start();
    }

    public void openMusicPlayer(){
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.android.music", "com.android.music.MusicBrowserActivity");
        intent.setComponent(comp);
        startActivity(intent);
    }
}

