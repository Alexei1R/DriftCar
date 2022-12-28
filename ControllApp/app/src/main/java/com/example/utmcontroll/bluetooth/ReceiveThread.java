package com.example.utmcontroll.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ReceiveThread extends Thread{
    private BluetoothSocket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private byte[] RXBuffer;

    public ReceiveThread(BluetoothSocket socket){
        this.socket = socket;
        try {
            inputStream = socket.getInputStream();
            Log.d("MyTog", "Try input stream");

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("MyTog", "Try input erorr stream");
        }


        try {
            outputStream = socket.getOutputStream();
            Log.d("MyTog", "Try output stream");

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("MyTog", "Try output erorr stream");
        }
    }

    @Override
    public void run() {
        RXBuffer = new byte[255];
        while (true){
            try{
                int length = inputStream.read(RXBuffer);
                String msg = new String(RXBuffer,0,length);
                Log.d("MyLog","Message: " + msg.toString());
            }catch (IOException ex){
                Log.d("MyLog","Error receive");
                break;
            }

        }
    }


    public  void SendData(String msg){
        try{
            outputStream.write(msg.getBytes(StandardCharsets.US_ASCII));
        }catch (IOException e){
            Log.d("MyLog" , "Error to send data");
        }
    }
}
