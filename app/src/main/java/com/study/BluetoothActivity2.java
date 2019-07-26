package com.study;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.study.base.BaseActivity;
import com.study.util.MLog;

/**
 * 作者：Tangren on 2019-07-26
 * 包名：com.study
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */
public class BluetoothActivity2 extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, CompoundButton.OnCheckedChangeListener {
    private Switch bluStatus;
    private SwipeRefreshLayout refresh;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothBroadcastReceiver bluetoothBroadcastReceiver;


    @Override
    protected int layoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bluStatus = findViewById(R.id.wifiStatus);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        refresh = findViewById(R.id.swipeRefreshLayout);
        bluStatus.setOnCheckedChangeListener(this);
    }

    @Override
    protected void initData() {
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "该设备不支持蓝牙", Toast.LENGTH_SHORT).show();
            bluStatus.setEnabled(false);
        } else {
            bluStatus.setChecked(bluetoothAdapter.isEnabled());
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        bluetoothBroadcastReceiver = new BluetoothBroadcastReceiver();
        registerReceiver(bluetoothBroadcastReceiver, filter);

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!buttonView.isPressed()) {
            return;
        }
        boolean isEnabled = bluetoothAdapter.isEnabled();
        bluStatus.setChecked(!isEnabled);
        if (isChecked) {
            bluetoothAdapter.enable();
            MLog.d("onCheckedChanged(BluetoothActivity.java:64)正在开启");
            Toast.makeText(this, "正在开启", Toast.LENGTH_SHORT).show();
            scanBluetoothDevice();
        } else {
            bluetoothAdapter.disable();
            MLog.d("onCheckedChanged(BluetoothActivity.java:70)正在关闭");
            Toast.makeText(this, "正在关闭", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 扫描蓝牙设备
     */
    private void scanBluetoothDevice() {
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothAdapter.startDiscovery();
    }


    class BluetoothBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                return;
            }

            switch (action) {
                case BluetoothDevice.ACTION_FOUND:
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    StringBuilder builder = new StringBuilder();
                    builder.append("name=").append(device.getName());
                    builder.append("state=").append(device.getBondState());
                    builder.append("type=").append(device.getType());
                    builder.append("address=").append(device.getAddress());
                    MLog.d("onReceive(BluetoothBroadcastReceiver.java:116)" + builder.toString());
                    break;
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    MLog.d("onReceive(BluetoothBroadcastReceiver.java:126)ACTION_STATE_CHANGED");
                    if (bluetoothAdapter.enable()){
                        MLog.d("onReceive(BluetoothBroadcastReceiver.java:128)扫描");
                        scanBluetoothDevice();
                    }
                    break;
                default:

                    break;
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bluetoothBroadcastReceiver!=null){
            unregisterReceiver(bluetoothBroadcastReceiver);
        }
    }
}
