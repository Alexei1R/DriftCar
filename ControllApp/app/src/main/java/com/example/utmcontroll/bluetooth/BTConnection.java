package com.example.utmcontroll.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.utmcontroll.adapter.BTConstd;

public class BTConnection {
    private BluetoothDevice device;
    private BluetoothAdapter adapter;
    private Context context;
    private SharedPreferences pref;
    private ConnectThread connectThread;

    public BTConnection(Context context){
        this.context = context;
        pref = context.getSharedPreferences(BTConstd.MY_PREF,Context.MODE_PRIVATE);
        adapter = BluetoothAdapter.getDefaultAdapter();

    }


    public void connect(){
        String mac = pref.getString(BTConstd.MAC_KEY,"");
        if(!adapter.isEnabled() || mac.isEmpty()){
            Log.d("MyLog", "Erorr bt connection");
            return;
        }
        device = adapter.getRemoteDevice(mac);
        if(device == null){return;}
        connectThread = new ConnectThread(context,adapter,device);
        connectThread.start();
    }

    public void  SendData(String msg){

        if (connectThread.getReceiveThread() != null)
            connectThread.getReceiveThread().SendData(msg);

        else {
            Log.d("MyLog","Receive thread is not ready");
        }


    }


}
