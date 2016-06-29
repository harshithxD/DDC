package com.example.harshith.ddc;

import android.app.Service;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
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

                        receiveDataThread = new ReceiveDataThread(bluetoothSocket,handler,globalClass,false);
                        receiveDataThread.start();
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
}

