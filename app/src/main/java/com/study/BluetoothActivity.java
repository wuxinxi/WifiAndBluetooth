package com.study;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.qindachang.bluetoothle.BluetoothLe;
import com.qindachang.bluetoothle.OnLeScanListener;
import com.qindachang.bluetoothle.exception.ScanBleException;
import com.qindachang.bluetoothle.scanner.ScanRecord;
import com.qindachang.bluetoothle.scanner.ScanResult;
import com.study.base.BaseActivity;
import com.study.util.MLog;

import java.util.List;

/**
 * 作者：Tangren on 2019-07-26
 * 包名：com.study
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */
public class BluetoothActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, CompoundButton.OnCheckedChangeListener {
    private Switch bluStatus;
    private SwipeRefreshLayout refresh;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;


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
        refresh.setOnRefreshListener(this);
        BluetoothLe mBluetoothLe = BluetoothLe.getDefault();
        mBluetoothLe.init(this);

        mBluetoothLe.setScanPeriod(15000)//设置扫描时长，单位毫秒，默认10秒
//                .setScanWithDeviceAddress("00:20:ff:34:aa:b3")//根据硬件地址过滤扫描
//                .setScanWithServiceUUID("6E400001-B5A3-F393-E0A9-E50E24DCCA9E")//设置根据服务uuid过滤扫描
//                .setScanWithDeviceName("ZG1616")//设置根据设备名称过滤扫描
                .setReportDelay(10)//如果为0，则回调onScanResult()方法，如果大于0, 则每隔你设置的时长回调onBatchScanResults()方法，不能小于0
                .startScan(this);

        mBluetoothLe.setOnScanListener(new OnLeScanListener() {
            @Override
            public void onScanResult(BluetoothDevice bluetoothDevice, int rssi, ScanRecord scanRecord) {
                MLog.d("onScanResult(BluetoothActivity.java:61)" + bluetoothDevice.getName());
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                MLog.d("onBatchScanResults(BluetoothActivity.java:68)"+results.toString());
            }

            @Override
            public void onScanCompleted() {
                MLog.d("onScanCompleted(BluetoothActivity.java:72)检索完成");
            }

            @Override
            public void onScanFailed(ScanBleException e) {
                MLog.d("onScanFailed(BluetoothActivity.java:77)失败");
            }
        });
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
    }

    @Override
    public void onRefresh() {
        refresh.setRefreshing(true);

        refresh.setRefreshing(false);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }


}
