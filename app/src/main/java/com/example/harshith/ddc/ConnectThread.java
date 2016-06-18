package com.example.harshith.ddc;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by harshith on 17/6/16.
 */

public class ConnectThread extends Thread {
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice bluetoothDevice;
    private BluetoothSocket bluetoothSocket;
    private String address = null;
    private UUID uuid = null;
    GlobalClass globalClass;
    public int isConnected;
    Handler handler;
    ReceiveDataThread receiveDataThread = null;

    public ConnectThread(String address,UUID uuid,Handler handler,GlobalClass globalClass){
        this.address = address;
        this.uuid = uuid;
        this.handler = handler;
        this.globalClass = globalClass;
    }

    @Override
    public void run() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
        try {
            bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect();
            isConnected = Constants.CONNECTION_STATUS_OK;
            receiveDataThread = new ReceiveDataThread(bluetoothSocket,handler,globalClass);
            receiveDataThread.start();

        }
        catch (IOException e) {
            isConnected = Constants.CONNECTION_STATUS_NOT_CONNECTED;
        }
        Looper.prepare();
        Message message = handler.obtainMessage(Constants.CONNECTION_STATUS,isConnected);
        handler.handleMessage(message);
        Looper.loop();
    }
}

