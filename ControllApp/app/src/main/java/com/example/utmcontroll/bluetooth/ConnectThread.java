package com.example.utmcontroll.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;

public class ConnectThread extends Thread{
    private BluetoothDevice device;
    private BluetoothAdapter adapter;
    private Context context;
    private BluetoothSocket socket;
    private ReceiveThread receiveThread;
    public static final String UUID = "00001101-0000-1000-8000-00805F9B34FB";




    @SuppressLint("MissingPermission")
    public ConnectThread(Context context, BluetoothAdapter adapter, BluetoothDevice device){
        this.context = context;
        this.adapter = adapter;
        this.device = device;
        try {
            socket = device.createRfcommSocketToServiceRecord(java.util.UUID.fromString(UUID));
        }catch (IOException e){}


    }


    @SuppressLint("MissingPermission")
    @Override
    public void run() {
        adapter.cancelDiscovery();
        try {
            socket.connect();
            receiveThread = new ReceiveThread(socket);
            receiveThread.start();
            Log.d("MyLog","Connected");
        }catch (IOException ex){
            Log.d("MyLog","Error Connect bluetooth");
            CloseConnection();
        }
    }
    public  void CloseConnection(){
        try{

            socket.close();

        }catch (IOException exe){}
    }

    public ReceiveThread getReceiveThread(){
        if(receiveThread != null)
            return receiveThread;
        else   return null;
    }
}
