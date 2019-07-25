package com.study.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.study.util.MLog;
import com.study.util.wifi.WifiUtil;

/**
 * 作者：Tangren on 2019-07-25
 * 包名：com.study.broadcast
 * 邮箱：996489865@qq.com
 * TODO:
 * WIFI_STATE_DISABLED    WLAN已经关闭
 * WIFI_STATE_DISABLING   WLAN正在关闭
 * WIFI_STATE_ENABLED     WLAN已经打开
 * WIFI_STATE_ENABLING    WLAN正在打开
 * WIFI_STATE_UNKNOWN     未知
 */
public class WifiBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //wifi状态监听
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            switch (state) {
                case WifiManager.WIFI_STATE_DISABLED:
                    MLog.e("onReceive(WifiBroadcastReceiver.java:28)wifi 已关闭");
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    MLog.e("onReceive(WifiBroadcastReceiver.java:31)wifi 正在打开");
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    MLog.e("onReceive(WifiBroadcastReceiver.java:35)wifi 已经打开");
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    MLog.e("onReceive(WifiBroadcastReceiver.java:38)wifi 正在打开");
                    break;
                default:
                    break;
            }
        } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (NetworkInfo.State.DISCONNECTED == info.getState()) {
                //wifi没连接上
                MLog.e("onReceive(WifiBroadcastReceiver.java:50)wifi没连接上");
            } else if (NetworkInfo.State.CONNECTED == info.getState()) {
                //wifi连接上了
                MLog.e("onReceive(WifiBroadcastReceiver.java:53)wifi连接上了");
                WifiInfo connectedWifiInfo = WifiUtil.getConnectedWifiInfo(context);

            } else if (NetworkInfo.State.CONNECTING == info.getState()) {
                //wifi正在连接
                MLog.e("onReceive(WifiBroadcastReceiver.java:58)wifi正在连接");
                WifiInfo connectedWifiInfo = WifiUtil.getConnectedWifiInfo(context);


            }
        } else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) {
            //网络列表变化了,重新扫描
            MLog.e("onReceive(WifiBroadcastReceiver.java:65)网络列表变化了,重新扫描");

        }
    }
}
