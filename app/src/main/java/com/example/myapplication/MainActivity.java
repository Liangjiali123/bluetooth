package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myapplication.model.BLEDevice;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<BLEDevice> data = null;
    private ListView listView;
    private BluetoothAdapter bluetoothAdapter = null;
    private BluetoothManager bluetoothManager = null;
    private Button button;
    private  BLEAdapter adapter = null;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);

        // 获得数据
        data = new ArrayList<>();
        // 初始化控件
        initView();

    }

    private void initView() {

        // 获得button
        button = findViewById(R.id.search);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 扫描蓝牙
                scanBle();
            }
        });

        // 初始化ListView
        adapter =  new BLEAdapter(MainActivity.this);
        listView = findViewById(R.id.lv);
        listView.setAdapter(adapter);

    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (action == null)
                return;

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (device == null)
                    return;

                String deviceName = device.getName();
                if (deviceName == null)
                    return;

                BLEDevice bleDevice = new BLEDevice(deviceName, device.getAddress(), device.getBondState());

                if (!data.contains(bleDevice)) {
                    data.add(bleDevice);
                    adapter.setDatas(data);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    };
    private static final String TAG = "MainActivitylog";

    // 扫描蓝牙
    private void scanBle() {
        // 初始化蓝牙适配器
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager == null) {
            return;
        }

        if (bluetoothAdapter == null)
            bluetoothAdapter = bluetoothManager.getAdapter();


        if (bluetoothAdapter.isDiscovering()) {
            boolean succeed = bluetoothAdapter.cancelDiscovery();
            Log.e(TAG, "scanBle:cancelDiscovery  succeed =" + succeed);

            data.clear();
            adapter.notifyDataSetChanged();
        } else {
            boolean succeed = bluetoothAdapter.startDiscovery();
            Log.e(TAG, "scanBle:startDiscovery  succeed =" + succeed);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver);
    }
}