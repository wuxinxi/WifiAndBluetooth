package com.study.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.study.R;
import com.study.base.BaseActivity;
import com.study.bluetooth.adapter.BluetoothAdapter;
import com.study.bluetooth.listener.OnBluetoothStateListener;
import com.study.bluetooth.listener.OnDiscoveryDeviceListener;
import com.study.bluetooth.manage.BluetoothManager;
import com.study.listener.IItemListener;
import com.study.util.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Tangren on 2019-07-27
 * 包名：com.study.bluetooth
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */
public class BluetoothActivity extends BaseActivity implements OnBluetoothStateListener, OnDiscoveryDeviceListener,
        CompoundButton.OnCheckedChangeListener, SwipeRefreshLayout.OnRefreshListener, IItemListener<BluetoothDevice> {
    private Switch bluStatus;
    private BluetoothManager bluetoothManager;
    private SwipeRefreshLayout refreshLayout;
    private BluetoothAdapter adapter;
    private List<BluetoothDevice> list = new ArrayList<>();


    @Override
    protected void onResume() {
        super.onResume();
        bluetoothManager.setOnDiscoveryDeviceListener(this);
        bluetoothManager.setOnBluetoothStateListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        bluetoothManager.removeOnBluetoothStateListener();
        bluetoothManager.removeOnDiscoveryDeviceListener();
    }

    @Override
    protected int layoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bluStatus = findViewById(R.id.wifiStatus);
        refreshLayout = findViewById(R.id.swipeRefreshLayout);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecycleViewDivider(this, RecyclerView.HORIZONTAL));
        bluStatus.setOnCheckedChangeListener(this);
        refreshLayout.setOnRefreshListener(this);

        adapter = new BluetoothAdapter(list,getApplicationContext());
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        bluetoothManager = new BluetoothManager();

    }

    @Override
    public void onBluetoothStateClosing() {

    }

    @Override
    public void onBluetoothStateColsed() {

    }

    @Override
    public void onBluetoothStateOpening() {

    }

    @Override
    public void onBluetoothStateOpened() {

    }

    @Override
    public void onDiscoveryDeviceStarted() {
        list = new ArrayList<>();
    }

    @Override
    public void onDiscoverDeviceFound(BluetoothDevice device) {
        if (!TextUtils.isEmpty(device.getName())) {
            list.add(device);
            adapter.addDatas(device);
        }
    }

    @Override
    public void onDiscoverDeviceComplete() {
        adapter.refreshDatas(list);
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            bluetoothManager.openBluetooth();
        } else {
            bluetoothManager.closeBluetooth();
        }
    }

    @Override
    public void onRefresh() {
        bluetoothManager.discovery();
    }

    @Override
    public void onItemClick(int position, BluetoothDevice device) {
        Toast.makeText(this, device.getName(), Toast.LENGTH_SHORT).show();
    }
}
