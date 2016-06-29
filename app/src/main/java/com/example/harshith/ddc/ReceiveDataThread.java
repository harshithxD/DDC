package com.example.harshith.ddc;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LoggingPermission;

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
            int indx; int [][]array;
            DynamicGesture []a = new DynamicGesture[noOfGestures];

            // initialising the 10 gestures

            indx=0;
//            array = new int[][]
//                    {
//                            { 90, 96, 92, 94, 100, 1942, 16036, -988, 1736, 15926, -528 },
//                            { 90, 96, 90, 92, 98, 1100, 16096, -282, 906, 16284, -164 },
//                            { 85, 96, 90, 92, 96, 914, 16214, -54, 1274, 16238, -236 } ,
//                            { 85, 94, 90, 89, 96, 1328, 16078, -82, 1184, 16408, -256 },
//                            { 80, 92, 88, 86, 94, 1004, 16016, -158, 1184, 15968, -346 },
//                            { 80, 90, 86, 81, 93, 1262, 15804, -176, 952, 15616, 38 },
//                            { 76, 88, 82, 76, 87, 854, 15820, 462, 940, 16080, 434 },
//                            { 71, 79, 74, 71, 84, 766, 16722, -90, 536, 16004, 60 },
//                            { 66, 73, 69, 65, 75, 922, 15938, 190, 1164, 16170, 210 },
//                            { 61, 67, 66, 60, 70, 1498, 16074, 286, 1134, 16042, 566 },
//                            { 61, 66, 62, 60, 65, 432, 16230, 766, 432, 16310, 706 },
//                            { 57, 62, 60, 55, 63, 916, 16092, 410, 778, 15850, 572 },
//                            { 57, 58, 58, 52, 58, 632, 15824, 790, 552, 16034, 872 },
//                            { 57, 56, 54, 50, 55, 690, 16208, 1040, 694, 16132, 954 },
//                            { 57, 52, 50, 47, 51, 644, 16104, 1136, 848, 15788, 886 },
//                            { 52, 49, 48, 44, 48, 900, 15890, 964, 818, 16166, 1006 },
//                            { 52, 45, 45, 39, 44, 790, 16286, 828, 894, 16004, 802 },
//                            { 47, 39, 40, 34, 41, 816, 15534, 670, 724, 15856, 864 },
//                            { 47, 32, 36, 31, 36, 692, 16274, 904, 800, 16146, 688 },
//                            { 42, 26, 30, 28, 32, 1204, 16032, 432, 1104, 15924, 382 },
//                            { 38, 20, 24, 23, 27, 862, 16080, 594, 308, 16028, 1072 },
//                            { 38, 16, 18, 18, 22, -192, 16002, 1148, 268, 15918, 1264 },
//                            { 33, 15, 14, 15, 17, 1284, 15888, 976, 1402, 15924, 1044 },
//                            { 28, 11, 10, 13, 10, 924, 16046, 1116, 668, 16112, 1078 },
//                            { 28, 9, 8, 10, 5, 602, 16180, 1292, 682, 16202, 1216 },
//                            { 28, 7, 6, 7, 5, 906, 15948, 442, 1234, 15856, 44 },
//                            { 19, 5, 2, 7, 1, 890, 16172, 672, 1072, 16340, 1196 },
//                            { 23, 5, 2, 5, 0, 1444, 16062, 1072, 1404, 16024, 490 },
//                            { 19, 3, 1, 5, 0, 1458, 15990, 328, 1244, 15848, 564 },
//                            { 14, 1, 0, 5, 0, 992, 16100, 1092, 1328, 15836, 1134 },
//                            { 14, 1, 0, 2, 0, 1708, 15950, 874, 1552, 15876, 1024 },
//                            { 4, 1, 0, 0, 0, 1410, 16110, 1012, 1334, 16220, 926 },
//                            { 0, 1, 0, 0, 0, 1814, 16118, 702, 2076, 16140, 674 },
//                            { 4, 3, 0, 0, 0, 2062, 16094, 438, 1614, 16294, 406 }
//                    };
            array = new int[101][11];
            for (int i = 0; i != 101;i++){
                for (int j = 0; j != 5;j++){
                    array[i][j] = i;
                }
                array[i][9] = 16000;
            }
            a[indx] = new DynamicGesture(array.length);
            int []cons = new int[11];
            cons = new int[] {1,1,1,1,1,0,0,0,0,1,0};
            a[indx].updateFrame(array,cons);
            a[indx].printData();


            indx = 1;
            array = new int[][]
                    {
                            { 4, 83, 88, 94, 94, 882, 2844, 14294, 206, 1894, 15050 },
                            { 0, 81, 86, 94, 93, -750, 1270, 15836, -912, 1604, 15940 },
                            { 0, 81, 85, 94, 93, 592, 3026, 14660, 628, 2050, 14218 },
                            { 0, 81, 85, 94, 91, -526, 2094, 15172, -858, 1784, 15512 },
                            { 4, 81, 85, 94, 93, -220, 1838, 14926, 352, 2142, 14472 },
                            { 4, 81, 82, 94, 91, -264, 2630, 13686, -1984, 2526, 13590 },
                            { 4, 81, 82, 92, 91, -4724, 2896, 13010, -8502, 4158, 10682 },
                            { 4, 79, 84, 92, 91, -19204, 7996, 7284, -30724, 8484, 6058 },
                            { 4, 79, 85, 92, 91, -32768, 4418, 8706, -32768, -274, 12506 },
                            { 4, 79, 85, 89, 91, -32768, -5068, 17128, -32768, -10092, 20266 },
                            { 4, 79, 82, 86, 87, -16616, -12792, 21146, -1464, -14152, 18000 },
                            { 4, 79, 82, 89, 87, 9646, -17732, 18678, 15100, -21382, 22950 },
                            { 4, 77, 82, 92, 87, 30192, -22396, 24002, 32767, -16526, 22208 },
                            { 9, 77, 81, 92, 87, 27054, -8232, 18114, 20222, -5738, 15942 },
                            { 9, 77, 82, 92, 87, 15016, -5922, 16380, 11628, -5202, 17620 },
                            { 9, 77, 81, 89, 86, 6194, -2642, 17878, 3768, -952, 16812 }
                    };
            a[indx] = new DynamicGesture(array.length);

            cons = new int[] {1,1,1,1,1,1,0,0,0,1,0};
            a[indx].updateFrame(array,cons);

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
                        Looper.prepare();
                        handler.obtainMessage(Constants.READ_STATUS,readStatus,i,null).sendToTarget();
                        L.m("Gesture " + i + " is executed");
                        q.gestureStatusPrint();
                        Looper.loop();
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
