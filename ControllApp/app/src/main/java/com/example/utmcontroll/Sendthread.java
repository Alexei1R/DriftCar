package com.example.utmcontroll;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.utmcontroll.bluetooth.BTConnection;
import com.example.utmcontroll.bluetooth.ReceiveThread;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Sendthread extends Thread{
    private SendPackageData data;
    private BTConnection btConnection;
    Integer[] arr;

    public Sendthread(SendPackageData data,BTConnection btConnection){
        this.data = data;
        this.btConnection = btConnection;
        arr = new Integer[6];

    }


    @SuppressLint("MissingPermission")
    @Override
    public void run() {
        try {
        while(true) {
            arr = data.GetPackageData();
            btConnection.SendData((byte) 112);
            for (int i = 0; i < 6; i++) {

                Integer val = arr[i];
                val = arr[i];
                byte[] bytes = ByteBuffer.allocate(4).putInt(val).array();
                for (int j = 0; j < 4; j++) {
                    btConnection.SendData(bytes[j]);
                }
            Thread.sleep(8);

            }
        }
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted.");
        }
    }

}

