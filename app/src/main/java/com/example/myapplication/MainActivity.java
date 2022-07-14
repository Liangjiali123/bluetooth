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
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> data = null;
    private ListView listView = null;
    private BluetoothAdapter bluetoothAdapter = null;
    private BluetoothManager bluetoothManager = null;
    private Button button = null;
    ArrayAdapter<String> adapter = null;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 获得数据
        data = new ArrayList<>();
        // 初始化控件
        initView();

    }

    private void initView() {
        // 获得button
        this.button = findViewById(R.id.search);
        this.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 扫描蓝牙
                scanBle();
            }
        });

        // 初始化ListView
        this.adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.data);
        this.listView = findViewById(R.id.lv);
        this.listView.setAdapter(adapter);

    }

    private BluetoothAdapter.LeScanCallback oldBtsc = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            device.getName();
            data.add("[" + device.getAddress() + "]");
            adapter.notifyDataSetChanged();
        }
    };

    // 扫描蓝牙
    private void scanBle() {
        // 初始化蓝牙适配器
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager == null) {
            return;
        }

        bluetoothAdapter = bluetoothManager.getAdapter();

        // 打开蓝牙
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enable, 1);
        } else {
            bluetoothAdapter.startLeScan(oldBtsc);
            // 设置扫描时间
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    bluetoothAdapter.stopLeScan(oldBtsc);
                }
            }, 8000);
        }
    }
}