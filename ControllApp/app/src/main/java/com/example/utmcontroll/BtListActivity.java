package com.example.utmcontroll;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.utmcontroll.adapter.BTAdapter;
import com.example.utmcontroll.adapter.ListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BtListActivity extends AppCompatActivity {
    private ListView listView;
    private BTAdapter adapter;
    private BluetoothAdapter btAdapter;
    private  List<ListItem> list;
    private  boolean isbtpermisiongranted = false;
    private  boolean isDiscovery = false;
    private  ActionBar ab;


    private final int BT_REQUEST_PERM = 1232;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt_list);
        getBTPermision();
        Init();

    }

    @SuppressLint("MissingPermission")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            if(isDiscovery){
                btAdapter.cancelDiscovery();
                isDiscovery = false;
                getPairedDevices();
            }else{
                finish();
            }





        }
        else if(item.getItemId() == R.id.id_search){
            if(isDiscovery)return true;
            ab.setTitle("Se cauta...");
            list.clear();
            ListItem itemTitle = new ListItem();
            itemTitle.setitemType(BTAdapter.TITLE_ITEM_TYPE);
            list.add(itemTitle);
            adapter.notifyDataSetChanged();
            btAdapter.startDiscovery();
            isDiscovery = true;
        }

        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter f1 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        IntentFilter f2 = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        IntentFilter f3 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(bReceiver,f1);
        registerReceiver(bReceiver,f2);
        registerReceiver(bReceiver,f3);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(bReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bt_listmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void Init() {
        ab = getSupportActionBar();
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        list = new ArrayList<>();
        ActionBar a_bar = getSupportActionBar();
        if (a_bar == null) {
            return;
        }
        a_bar.setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.list_wiew);


        adapter = new BTAdapter(this, R.layout.bt_list_item, list);
        listView.setAdapter(adapter);
        getPairedDevices();
        onItemClickListener();

    }




    private void onItemClickListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                ListItem item = (ListItem)parent.getItemAtPosition(i);
                if(item.getitemType() == BTAdapter.DEF_ITEM_TYPE){
                    item.getBtDevice().createBond();
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getPairedDevices() {
        @SuppressLint("MissingPermission")
        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            list.clear();
            for (BluetoothDevice device : pairedDevices) {

                @SuppressLint("MissingPermission")
                        ListItem item = new ListItem();
                item.setBtDevice(device);
                list.add(item);

            }
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == BT_REQUEST_PERM){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                isbtpermisiongranted = true;
                Toast.makeText(this,"Este permisa cautarea de bluethout elemente",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this,"Nu este permisa cautarea de bluethout elemente",Toast.LENGTH_LONG).show();
            }
        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    private  void getBTPermision(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},BT_REQUEST_PERM);
        }
        else{
            isbtpermisiongranted = true;
        }
    }




    private final BroadcastReceiver bReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(BluetoothDevice.ACTION_FOUND.equals(intent.getAction())){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                ListItem item = new ListItem();
                item.setBtDevice(device);
                item.setitemType(BTAdapter.DISCOVERY_ITEM_TYPE);
                list.add(item);
                adapter.notifyDataSetChanged();
                //Toast.makeText(context,"Sau gasit elemnte : " + device.getName(),Toast.LENGTH_LONG).show();
            }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(intent.getAction())){
                isDiscovery = false;

                getPairedDevices();

                ab.setTitle("BTControll");
            }else if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(intent.getAction())){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device.getBondState() == BluetoothDevice.BOND_BONDED){
                    getPairedDevices();
                }
            }
        }
    };

}