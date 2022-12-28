package com.example.utmcontroll.adapter;

import android.bluetooth.BluetoothDevice;

public class ListItem {


    private BluetoothDevice btDevice;
    private String itemType = BTAdapter.DEF_ITEM_TYPE;


    public BluetoothDevice getBtDevice() {
        return btDevice;
    }

    public void setBtDevice(BluetoothDevice btDevice) {
        this.btDevice = btDevice;
    }

    public void setitemType(String itemType) {this.itemType = itemType;}
    public String getitemType() {return itemType;}



}
