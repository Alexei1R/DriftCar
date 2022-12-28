package com.example.utmcontroll;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utmcontroll.adapter.BTConstd;
import com.example.utmcontroll.bluetooth.BTConnection;

public class MainActivity extends AppCompatActivity {
    private MenuItem menuItem;
    private BluetoothAdapter btAdapter;
    private final int ENABLE_REQUEST = 156;
    private SharedPreferences pref;
    private BTConnection btConnection;

    
    private SensorManager sm;
    private Sensor s;
    private ImageView imView;
    private TextView textView;
    private TextView throtleView;
    private RelativeLayout relativeLayout;
    private ImageView joystick;




    private int lastPosY = 0;
    private int lastPosX = 0;
    private int deltaX = 0;
    private int deltaY = 0;


    private volatile boolean whileThread = false;


    private SensorEventListener sv;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
        textView = findViewById(R.id.bara_grade);
        imView = findViewById(R.id.imGrage);
        throtleView = findViewById(R.id.throtle);
        relativeLayout = findViewById(R.id.relativeLayout);
        joystick = findViewById(R.id.joystick);


        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sm != null) {
            s = sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        }


        processMotion();

       /* new Thread() {
            @Override
            public void run() {
                super.run();
                while (!whileThread) {
                    try {
                        Thread.sleep(3000);
                        btConnection.SendMessage("a");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }
        }.start();*/

    }


    @SuppressLint("ClickableViewAccessibility")
    void processMotion() {
        sv = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float[] rotationMatrix = new float[16];
                SensorManager.getRotationMatrixFromVector(rotationMatrix, sensorEvent.values);
                float[] remapedRotationMatrix = new float[16];
                SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Z, remapedRotationMatrix);

                float[] orientations = new float[3];
                SensorManager.getOrientation(remapedRotationMatrix, orientations);
                for (int i = 0; i < 3; i++) {
                    orientations[i] = -(float) (Math.toDegrees(orientations[i]));
                }
                textView.setText((String.valueOf((int) orientations[2])));
                

                imView.setRotation(orientations[2] - 90);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }


        };

        relativeLayout.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                lastPosX = (int) motionEvent.getX();
                lastPosY = (int) motionEvent.getY();
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                deltaX = (int) motionEvent.getX() - lastPosX;
                deltaY = (int) motionEvent.getY() - lastPosY;
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                lastPosX = 0;
                lastPosY = 0;
                deltaX = 0;
                deltaY = 0;
            }

            btConnection.SendData(  String.valueOf(deltaY));
            throtleView.setText(String.valueOf(deltaX) + " : " + String.valueOf(deltaY));
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(sv, s, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(sv);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menuItem = menu.findItem(R.id.id_bt_button);


        SetBtIcon();


        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("MissingPermission")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.id_bt_button) {
            if (!btAdapter.isEnabled()) {
                EnableBt();
                btAdapter.enable();
                SetBtIcon();
            } else {
                menuItem.setIcon(R.drawable.blu_en);
                btAdapter.disable();

            }
        } else if (item.getItemId() == R.id.id_bt_list_button) {
            if (btAdapter.isEnabled()) {
                Intent i = new Intent(MainActivity.this, BtListActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(this, "Enable Bluetooth", Toast.LENGTH_SHORT).show();
            }

        } else if (item.getItemId() == R.id.id_connection_menu) {
            Toast.makeText(this,"BUTTON connect apasat",Toast.LENGTH_LONG).show();
            Log.d("MyLog","btn pressed");
            btConnection.connect();

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ENABLE_REQUEST) {
            if (resultCode == RESULT_OK) {

                SetBtIcon();

            }

        }
    }

    @SuppressLint("MissingPermission")
    private void EnableBt() {
        Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(i, ENABLE_REQUEST);
    }

    private void SetBtIcon() {
        if (btAdapter.isEnabled()) {
            menuItem.setIcon(R.drawable.blu_dis);
        } else {
            menuItem.setIcon(R.drawable.blu_en);
        }
    }

    private void Init() {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        pref = getSharedPreferences(BTConstd.MY_PREF, MODE_PRIVATE);
        btConnection = new BTConnection(this);

    }

}