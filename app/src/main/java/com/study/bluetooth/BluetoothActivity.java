package com.study.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.study.R;
import com.study.base.BaseActivity;
import com.study.bluetooth.adapter.BluetoothAdapter;
import com.study.bluetooth.listener.OnBluetoothStateListener;
import com.study.bluetooth.listener.OnConnectionListener;
import com.study.bluetooth.listener.OnDiscoveryDeviceListener;
import com.study.bluetooth.manage.BluetoothManager;
import com.study.listener.IItemListener;
import com.study.util.MLog;
import com.study.util.RecycleViewDivider;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Tangren on 2019-07-27
 * 包名：com.study.bluetooth
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */
public class BluetoothActivity extends BaseActivity implements OnBluetoothStateListener, OnDiscoveryDeviceListener, OnConnectionListener,
        CompoundButton.OnCheckedChangeListener, IItemListener<BluetoothDevice>, View.OnClickListener {
    private Switch bluStatus;
    private BluetoothManager bluetoothManager;
    private ProgressBar progressBar;
    private Button refresh;
    private BluetoothAdapter boundAdapter;
    private BluetoothAdapter adapter;
    private List<BluetoothDevice> list = new ArrayList<>();
    private List<BluetoothDevice> boundList = new ArrayList<>();

    @Override
    protected void onResume() {
        super.onResume();
        bluetoothManager.setOnDiscoveryDeviceListener(this);
        bluetoothManager.setOnBluetoothStateListener(this);
        bluetoothManager.setOnConnectionListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        bluetoothManager.removeOnBluetoothStateListener();
        bluetoothManager.removeOnDiscoveryDeviceListener();
        bluetoothManager.removeOnConnectionListener();
    }

    @Override
    protected int layoutID() {
        return R.layout.activity_bluetooth;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bluStatus = findViewById(R.id.wifiStatus);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerView recyclerViewBound = findViewById(R.id.recyclerViewBound);
        progressBar = findViewById(R.id.progressBar);
        refresh = findViewById(R.id.refresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecycleViewDivider(this, RecyclerView.HORIZONTAL));
        recyclerViewBound.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBound.addItemDecoration(new RecycleViewDivider(this, RecyclerView.HORIZONTAL));
        bluStatus.setOnCheckedChangeListener(this);
        refresh.setOnClickListener(this);
        adapter = new BluetoothAdapter(list, getApplicationContext());
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);

        boundAdapter = new BluetoothAdapter(boundList, getApplicationContext());
        boundAdapter.setListener(this);
        recyclerViewBound.setAdapter(boundAdapter);
    }

    @Override
    protected void initData() {
        bluetoothManager = new BluetoothManager();
        bluStatus.setChecked(bluetoothManager.isEnabled());
        if (bluetoothManager.isEnabled()) {
            bluetoothManager.discovery();
            boundList.addAll(bluetoothManager.getBounderDevices());
            boundAdapter.refreshDatas(boundList);
        }
    }

    @Override
    public void onBluetoothStateClosing() {

    }

    @Override
    public void onBluetoothStateColsed() {
        adapter.clear();
    }

    @Override
    public void onBluetoothStateOpening() {

    }

    @Override
    public void onBluetoothStateOpened() {
        bluetoothManager.discovery();
    }

    @Override
    public void onDiscoveryDeviceStarted() {
        progressBar.setVisibility(View.VISIBLE);
        list = new ArrayList<>();
    }

    @Override
    public void onDiscoverDeviceFound(BluetoothDevice device) {
        if (!TextUtils.isEmpty(device.getName()) &&
                device.getBondState() != BluetoothDevice.BOND_BONDED) {
            list.add(device);
            adapter.addDatas(device);
        }

    }

    @Override
    public void onDiscoverDeviceComplete() {
        adapter.refreshDatas(list);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!buttonView.isPressed()) {
            return;
        }
        if (isChecked) {
            bluetoothManager.openBluetooth();
        } else {
            bluetoothManager.closeBluetooth();
        }
    }


    @Override
    public void onItemClick(int position, BluetoothDevice device) {
        try {
            android.bluetooth.BluetoothAdapter bluetoothAdapter = bluetoothManager.getBluetoothAdapter();
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            }
            if (device.getBondState() == BluetoothDevice.BOND_NONE) {
                MLog.d("开始配对");
                Method method = BluetoothDevice.class.getMethod("createBond");
                method.invoke(device);
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("已配对的设备")
                        .setNegativeButton("取消配对", (dialog, which) -> {
                            try {
                                Method method = BluetoothDevice.class.getMethod("removeBond", (Class[]) null);
                                method.invoke(device, (Object[]) null);
                                Toast.makeText(BluetoothActivity.this, "配对已取消", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(BluetoothActivity.this, "配对取消失败", Toast.LENGTH_SHORT).show();
                            }
                        }).setPositiveButton("连接", (dialog, which) -> {
                    MLog.d("开始连接");
                    new ConnectThread(device).start();
                }).show();

            }
        } catch (Exception e) {
            e.printStackTrace();
            MLog.e(e);
        }
    }


    @Override
    public void onPairing(BluetoothDevice device) {
        Toast.makeText(this, "配对中", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPairSuccess(BluetoothDevice device) {
        Toast.makeText(this, "配对成功", Toast.LENGTH_SHORT).show();
        boundAdapter.addDatas(device);
        new ConnectThread(device).start();
        //配对成功将其移出可用设备中
        if (list!=null&&!list.isEmpty()){
            if (list.contains(device)){
                list.remove(device);
                adapter.refreshDatas(list);
            }
        }

    }

    @Override
    public void onPairFail(BluetoothDevice device, Exception e) {
        if (boundList != null && !boundList.isEmpty()) {
            if (boundList.contains(device)) {
                boundList.remove(device);
                boundAdapter.refreshDatas(boundList);
            }
        }
        //取消配对后刷新列表
        bluetoothManager.discovery();
    }

    @Override
    public void onConnecting() {

    }

    @Override
    public void onConnectSuccess(BluetoothDevice device) {

    }

    @Override
    public void onConnectFail(Exception e) {

    }

    @Override
    public void onConnectLost(Exception e) {

    }

    @Override
    public void onClick(View v) {
        bluetoothManager.discovery();

    }

    class ConnectThread extends Thread {
        private BluetoothDevice device;
        private BluetoothSocket connectSocket;

        public ConnectThread(BluetoothDevice device) {
            this.device = device;
        }

        @Override
        public void run() {
            super.run();
            try {
                ParcelUuid[] uuids = device.getUuids();
                connectSocket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                connectSocket.connect();
            } catch (Exception e) {
                e.printStackTrace();
                MLog.e(e);
            }

        }
    }
}
