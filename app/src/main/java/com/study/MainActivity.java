package com.study;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.study.adapter.RecycleViewDivider;
import com.study.adapter.WifiAdapter;
import com.study.base.BaseActivity;
import com.study.dialog.CommonAlertDialog;
import com.study.entity.WifiResult;
import com.study.interfaces.IItemListener;
import com.study.util.MLog;
import com.study.util.wifi.WifiUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener,
        SwipeRefreshLayout.OnRefreshListener, IItemListener<WifiResult> {
    private Switch wifiStatus;
    private SwipeRefreshLayout refresh;
    private WifiManager wifiManager;
    private WifiAdapter mAdapter;
    private List<WifiResult> list = new ArrayList<>();
    private WifiBroadcastReceiver receiver;
    private WifiHandler mHandler;


    @Override
    protected int layoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        wifiStatus = findViewById(R.id.wifiStatus);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        refresh = findViewById(R.id.swipeRefreshLayout);
        wifiStatus.setOnCheckedChangeListener(this);
        refresh.setProgressViewOffset(false, 0, 48);
        refresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        refresh.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new WifiAdapter(list, getApplicationContext());
        recyclerView.addItemDecoration(new RecycleViewDivider(this,RecyclerView.HORIZONTAL));
        recyclerView.setAdapter(mAdapter);
        mAdapter.setListener(this);
    }

    @Override
    protected void initData() {
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        boolean isWifiEnabled = wifiManager.isWifiEnabled();
        wifiStatus.setChecked(isWifiEnabled);

        IntentFilter filter = new IntentFilter();
        //监听wifi是开关变化的状态
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        //监听wifi连接状态广播,是否连接了一个有效路由
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        //监听wifi列表变化（开启一个热点或者关闭一个热点）
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        receiver = new WifiBroadcastReceiver();
        registerReceiver(receiver, filter);
        mHandler = new WifiHandler(this);
        refreshWifi();
    }

    private void refreshScanWifi(String wifiName, String status) {
        boolean isWifiEnabled = wifiManager.isWifiEnabled();
        if (isWifiEnabled) {
            updateWifiStatus(wifiName, status);
        }
    }

    /**
     * 刷新wifi
     */
    public void refreshWifi() {
        boolean isWifiEnabled = wifiManager.isWifiEnabled();
        if (isWifiEnabled) {
            mHandler.post(() -> {
                refresh.setRefreshing(true);
                scanWifi();
            });

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!buttonView.isPressed()) {
            return;
        }
        boolean isWifiEnabled = wifiManager.isWifiEnabled();
        wifiStatus.setChecked(!isWifiEnabled);
        wifiManager.setWifiEnabled(!isWifiEnabled);
        if (isChecked) {
            refreshWifi();
        }else {
            mAdapter.clear();
        }
    }

    /**
     * 根据wifi名称修改连接状态
     * @param wifiName 目前连接的wifi 不一定是成功
     * @param status 连接状态
     */
    private void updateWifiStatus(String wifiName, String status) {
        for (WifiResult wifiResult : list) {
            String state = WifiUtil.wifiConnectStatus(wifiResult.ssid, getApplicationContext());
            if (!TextUtils.isEmpty(wifiName)) {
                if (TextUtils.equals("\"" + wifiResult.ssid + "\"", wifiName)) {
                    state = status;
                }
            } else {
                if (!TextUtils.isEmpty(status)) {
                    state = status;
                }
            }
            wifiResult.state = state;
            mAdapter.notifyDataSetChanged();
        }
    }


    /**
     * 刷新
     * 扫描有点耗时，在子线程中处理
     */
    private void scanWifi() {
        MLog.d("开始扫描wifi列表");
        List<ScanResult> scanResults = wifiManager.getScanResults();
        list.clear();
        MLog.d("扫描个数："+scanResults.size());
        WifiInfo connectedWifiInfo = WifiUtil.getConnectedWifiInfo(getApplicationContext());
        String wifiName=null;
        if (connectedWifiInfo!=null){
            wifiName=connectedWifiInfo.getSSID();
        }
        for (ScanResult scanResult : scanResults) {
            String state = WifiUtil.wifiConnectStatus(scanResult.SSID, getApplicationContext());
            if (TextUtils.equals("\"" +scanResult.SSID+"\"" ,wifiName)){
                state="已连接";
            }
            WifiResult wifiResult = new WifiResult(scanResult.SSID, WifiManager.calculateSignalLevel(scanResult.level,5),
                    state, scanResult.capabilities);
            list.add(wifiResult);
        }
        Collections.sort(list);
        mAdapter.refreshDatas(list);
        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
        }
        MLog.d("wifi列表扫描完成");
    }


    @Override
    public void onRefresh() {
        refresh.setRefreshing(true);
        refreshWifi();
    }

    @Override
    public void onItemClick(int position, WifiResult scanResult) {
        String capabilities = scanResult.capabilities;
        //无需密码
        if (WifiUtil.getWifiCipher(capabilities) == WifiUtil.WifiCipherType.WIFICIPHER_NOPASS) {
            WifiConfiguration tempConfig = WifiUtil.isExist(scanResult.ssid, getApplicationContext());
            if (tempConfig == null) {
                WifiConfiguration exist = WifiUtil.createWifiConfig(scanResult.ssid, null,
                        WifiUtil.WifiCipherType.WIFICIPHER_NOPASS);
                WifiUtil.addNetWork(exist, MainActivity.this);
            } else {
                WifiUtil.addNetWork(tempConfig, MainActivity.this);
            }
        } else {
            //输入密码
            WifiConfiguration tempConfig = WifiUtil.isExist(scanResult.ssid, getApplicationContext());
            if (tempConfig != null) {
                new AlertDialog.Builder(this)
                        .setTitle(scanResult.ssid)
                        .setMessage("安全性\n" + scanResult.capabilities)
                        .setNegativeButton("取消", (dialog, which) -> {

                        })
                        .setNeutralButton("取消保存", (dialog, which) -> {
                            wifiManager.removeNetwork(tempConfig.networkId);
                            refreshScanWifi(null, null);
                        })
                        .setPositiveButton("连接", (dialog, which) -> WifiUtil.addNetWork(tempConfig, getApplicationContext())).show();
                return;
            }
            CommonAlertDialog dialog = new CommonAlertDialog(MainActivity.this).setOnDialogListener(content -> {
                WifiConfiguration wifiConfiguration = WifiUtil.createWifiConfig(scanResult.ssid, content, WifiUtil.getWifiCipher(capabilities));
                WifiUtil.addNetWork(wifiConfiguration, getApplicationContext());
            });
            dialog.show();
        }
    }

    static class WifiHandler extends Handler {
        private WeakReference<MainActivity> weakReference;

        public WifiHandler(MainActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity activity = weakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    default:
                        break;
                }
            }
        }
    }

    class WifiBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                return;
            }
            if(WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())){
                int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                switch (state){
                    case WifiManager.WIFI_STATE_DISABLED:
                        MLog.d("wifi处于关闭状态");
                        break;
                    case WifiManager.WIFI_STATE_DISABLING:
                        MLog.d("正在关闭wifi");
                        break;
                    case WifiManager.WIFI_STATE_ENABLED:
                        MLog.d("已打开wifi");
                        Toast.makeText(context, "已打开wifi", Toast.LENGTH_SHORT).show();
                        //防止刚打开wifi检索到为0
                        mHandler.postDelayed(MainActivity.this::scanWifi,1000);
                        break;
                    case WifiManager.WIFI_STATE_ENABLING:
                        MLog.d("正在打开wifi");
                        mHandler.post(()-> refresh.setRefreshing(true));
                        Toast.makeText(context, "正在打开wifi", Toast.LENGTH_SHORT).show();
                        break;
                    case WifiManager.WIFI_STATE_UNKNOWN:
                        Toast.makeText(MainActivity.this,"未知状态",Toast.LENGTH_SHORT).show();
                        break;

                }
            }else  if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (NetworkInfo.State.DISCONNECTED == info.getState()) {
                    //wifi没连接上
                    MLog.e("onReceive(WifiBroadcastReceiver.java:50)wifi没连接上");
                    WifiInfo connectedWifiInfo = WifiUtil.getConnectedWifiInfo(context);
                    refreshScanWifi(connectedWifiInfo != null ? connectedWifiInfo.getSSID() : null, "连接失败");
                } else if (NetworkInfo.State.CONNECTED == info.getState()) {
                    //wifi连接上了
                    MLog.e("onReceive(WifiBroadcastReceiver.java:53)wifi连接上了");
                    WifiInfo connectedWifiInfo = WifiUtil.getConnectedWifiInfo(context);
                    refreshScanWifi(connectedWifiInfo != null ? connectedWifiInfo.getSSID() : null, "已连接");
                } else if (NetworkInfo.State.CONNECTING == info.getState()) {
                    //wifi正在连接
                    MLog.e("onReceive(WifiBroadcastReceiver.java:58)wifi正在连接");
                    WifiInfo connectedWifiInfo = WifiUtil.getConnectedWifiInfo(context);
                    refreshScanWifi(connectedWifiInfo.getSSID(), "正在连接...");

                }
            } else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) {
                //网络列表变化了,重新扫描
                MLog.e("onReceive(WifiBroadcastReceiver.java:65)网络列表变化了,重新扫描");
                refreshWifi();
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
