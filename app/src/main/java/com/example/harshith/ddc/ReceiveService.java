package com.example.harshith.ddc;

import android.app.Service;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.UUID;

/**
 * Created by harshith on 17/6/16.
 */

public class ReceiveService extends Service {
    ConnectThread connectThread;
    ReceiveDataThread receiveDataThread;
    private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    GlobalClass globalClass;
    public Handler handler;

    public Handler parentActivityHandler = null;
    private final IBinder mIBinder = new LocalBinder();

    @Override
    public void onCreate() {

        handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                if(message.what == Constants.CONNECTION_STATUS) {
                    if(message.arg1 == Constants.CONNECTION_STATUS_OK){
                        Toast.makeText(getApplicationContext(),"Connected",Toast.LENGTH_LONG).show();
//                        receiveDataThread = new ReceiveDataThread((BluetoothSocket) message.obj,handler,globalClass, getApplicationContext());
//                        receiveDataThread = new ReceiveDataThread((BluetoothSocket) message.obj, getApplicationContext());
//                        receiveDataThread.start();
                        parentActivityHandler.sendMessage(message);
                    }
                    else if((int) message.obj == Constants.CONNECTION_STATUS_NOT_CONNECTED){
                        Toast.makeText(getApplicationContext(),"Couldn't Connect to Dextera Domini, Check whether it is switched on",Toast.LENGTH_SHORT).show();
                    }
                }
                else if (message.what == Constants.READ_STATUS) {

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
        return mIBinder;
    }

    public class LocalBinder extends Binder
    {
        public ReceiveService getInstance()
        {
            return ReceiveService.this;
        }
    }

    public void setParentHandler(Handler handler){
        parentActivityHandler = handler;
    }

    public void spawnRecognitionThread(Message message){
        receiveDataThread = new ReceiveDataThread((BluetoothSocket) message.obj, handler, getApplicationContext());
        receiveDataThread.start();

    }

    public void checkWorking() {
        L.m("This is a call from Learning Activity");
    }

}

