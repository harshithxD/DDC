package com.example.harshith.ddc;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by harshith on 17/6/16.
 */

public class MainActivity extends AppCompatActivity {

    public static String EXTRA_DEVICE_ADDRESS;
    TextView textConnectionStatus;
    ListView pairedListView;

    Handler handler;
    ReceiveService mrService = null;
    private boolean isBound;

    Message connectionMessage = null;

    private BluetoothAdapter mBluetoothAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textConnectionStatus = (TextView) findViewById(R.id.connecting);
        textConnectionStatus.setTextSize(40);
        textConnectionStatus.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this,R.layout.device_name);


        pairedListView = (ListView) findViewById(R.id.paired_devices);
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {

                Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_LONG);
                connectionMessage = msg;
//                if(msg.obj.toString().equals("Connected")){
//                    textConnectionStatus.setText("Connected");
//                }
//                else {
//                    textConnectionStatus.setText("Connection Failed");
//                }
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();

        checkBTState();

        mPairedDevicesArrayAdapter.clear();

        textConnectionStatus.setText(" ");

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        try {
            if (mBluetoothAdapter != null){
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

                if(pairedDevices.size() > 0) {
                    findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
                    for(BluetoothDevice bluetoothDevice : pairedDevices) {
                        mPairedDevicesArrayAdapter.add(bluetoothDevice.getName() + "\n" + bluetoothDevice.getAddress());

                    }
                }
                else {
                    mPairedDevicesArrayAdapter.add("No devices paired");
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void checkBTState() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null) {
            Toast.makeText(this,"This device doesn't support bluetooth.",Toast.LENGTH_SHORT).show();
//            finish();
        }
        else {
            if(!mBluetoothAdapter.isEnabled()){
                Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBTIntent,1);
            }
        }
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            textConnectionStatus.setText("Connecting...");

            String info = ((TextView) view).getText().toString();
            String address = info.substring(info.length() - 17);

//            Intent i = new Intent(MainActivity.this,ReceiveActivity.class);
//            i.putExtra(EXTRA_DEVICE_ADDRESS,address);
//            startActivity(i);
            Intent i = new Intent(getBaseContext(),ReceiveService.class);
            i.putExtra(EXTRA_DEVICE_ADDRESS,address);
            startService(i);
            doBindService();

//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    textConnectionStatus.setText("");
//                }
//            },5000);
        }
    };

    private ServiceConnection mrServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mrService = ((ReceiveService.LocalBinder)iBinder).getInstance();
            mrService.setParentHandler(handler);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mrService = null;
        }
    };

    private void doBindService() {
        bindService(new Intent(this, ReceiveService.class), mrServiceConn, BIND_AUTO_CREATE);
        isBound = true;
    }

    private void doUnBindService() {
        if (isBound){
            unbindService(mrServiceConn);
            isBound = false;
        }
    }

    public void navigateLearning(View view) {
//        if (connectionMessage != null){
        if (true){
            Intent intent = new Intent(getApplicationContext(), LearningActivity.class);
            intent.putExtra("DEVICE_CONNECTION_DATA", (Parcelable) connectionMessage);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "Device not connected", Toast.LENGTH_SHORT).show();
        }

    }

    public void startDetecting(View view) {
//        if (connectionMessage != null){
        if (true){
            mrService.spawnRecognitionThread(connectionMessage);
        }
        else {
            Toast.makeText(getApplicationContext(), "Device not connected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnBindService();
    }
}

