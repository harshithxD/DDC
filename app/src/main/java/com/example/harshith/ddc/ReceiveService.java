package com.example.harshith.ddc;

import android.app.Instrumentation;
import android.app.Service;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
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

import java.io.DataOutputStream;
import java.io.IOException;
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
                                home();
                                break;
                            case 1:
                                back();
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
                                nextSong();
                                break;
                            case 7:
                                previousSong();
                                break;
                            case 8:
                                okGoogle();
                                break;
                            case 9:
                                openCamera();
                                break;
                            case 10:
                                cameraClick();
                                break;
                            case 11:
                                gallery();
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

    public void back(){
        Thread t= new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                }
                catch (Exception e){

                }
            }
        });
        t.start();
    }
    public void home(){
        inputKeyEvent("" + KeyEvent.KEYCODE_HOME);
    }
    public void nowOnTap(){
        inputLongPressKeyEvent("" + KeyEvent.KEYCODE_HOME);
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

    public void cameraClick(){
        inputKeyEvent("" + KeyEvent.KEYCODE_CAMERA);
    }

    public void gallery(){
        if(checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == PackageManager.PERMISSION_DENIED){
            L.s(getBaseContext(),"Please grant Permission to read storage data");
        }
        else {
            // Get last taken photo
            String[] projection = new String[]{
                    MediaStore.Images.ImageColumns._ID,
                    MediaStore.Images.ImageColumns.DATA,
                    MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.ImageColumns.DATE_TAKEN,
                    MediaStore.Images.ImageColumns.MIME_TYPE
            };
            final Cursor cursor = getContentResolver()
                    .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                            null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

            // Open in Gallery
            if (cursor.moveToFirst()) {
                String imageLocation = cursor.getString(1);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("file://" + imageLocation), "image/*");
                startActivity(intent);
            }
        }
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




    public void openMusicPlayer(){
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.android.music", "com.android.music.MusicBrowserActivity");
        intent.setComponent(comp);
        startActivity(intent);
    }
    public void audioPlayPause(){
        AudioManager audioManager = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
        KeyEvent downEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
        audioManager.dispatchMediaKeyEvent(downEvent);
        KeyEvent upEvent = new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
        audioManager.dispatchMediaKeyEvent(upEvent);
    }

    public void nextSong(){
        AudioManager audioManager = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
        KeyEvent downEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_NEXT);
        audioManager.dispatchMediaKeyEvent(downEvent);
        KeyEvent upEvent = new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_MEDIA_NEXT);
        audioManager.dispatchMediaKeyEvent(upEvent);
    }
    public void previousSong(){
        AudioManager audioManager = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
        KeyEvent downEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PREVIOUS);
        audioManager.dispatchMediaKeyEvent(downEvent);
        KeyEvent upEvent = new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_MEDIA_PREVIOUS);
        audioManager.dispatchMediaKeyEvent(upEvent);
    }

    public static void inputKeyEvent(final String keyCodeString) {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                int keyCode = Integer.parseInt(keyCodeString);
                try {
                    Process processKeyEvent = Runtime.getRuntime().exec("/system/xbin/su");
                    DataOutputStream os = new DataOutputStream(processKeyEvent.getOutputStream());
                    os.writeBytes("input keyevent " + keyCode + "\n");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        thread.start();

    }

    public static void inputLongPressKeyEvent(final String keyCodeString) {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                int keyCode = Integer.parseInt(keyCodeString);
                try {
                    Process processKeyEvent = Runtime.getRuntime().exec("/system/xbin/su");
                    DataOutputStream os = new DataOutputStream(processKeyEvent.getOutputStream());
                    os.writeBytes("input keyevent --longpress " + keyCode + "\n");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        thread.start();

    }
}

