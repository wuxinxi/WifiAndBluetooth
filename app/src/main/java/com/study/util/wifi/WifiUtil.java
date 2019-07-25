package com.study.util.wifi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.study.util.MLog;

import java.util.List;

/**
 * 作者：Tangren on 2019-07-25
 * 包名：com.study.util.wifi
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */
public class WifiUtil {
    public static final String WIFI_STATE_CONNECT = "已连接";
    public static final String WIFI_STATE_ON_CONNECTING = "正在连接";
    public static final String WIFI_STATE_UNSAVED = "未保存";
    public static final String WIFI_STATE_SAVED = "已保存";

    public enum WifiCipherType {
        WIFICIPHER_WEP, WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID
    }

    /**
     * 判断wifi热点支持的加密方式
     */
    public static WifiCipherType getWifiCipher(String s) {
        if (s.isEmpty()) {
            return WifiCipherType.WIFICIPHER_INVALID;
        } else if (s.contains("WEP")) {
            return WifiCipherType.WIFICIPHER_WEP;
        } else if (s.contains("WPA") || s.contains("WPA2") || s.contains("WPS")) {
            return WifiCipherType.WIFICIPHER_WPA;
        } else {
            return WifiCipherType.WIFICIPHER_NOPASS;
        }
    }

    /**
     * @param SSID     wifi名称
     * @param password 密码
     * @param type     加密类型
     * @return wifi配置
     */
    public static WifiConfiguration createWifiConfig(String SSID, String password, WifiCipherType type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";
        if (type == WifiCipherType.WIFICIPHER_NOPASS) {
//            config.wepKeys[0] = "";  //注意这里
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//            config.wepTxKeyIndex = 0;
        }
        if (type == WifiCipherType.WIFICIPHER_WEP) {
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (type == WifiCipherType.WIFICIPHER_WPA) {
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;

        }
        return config;
    }

    /**
     * 接入某个wifi热点
     */
    public static boolean addNetWork(WifiConfiguration config, Context context) {
        WifiManager wifimanager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiinfo = wifimanager.getConnectionInfo();
        if (null != wifiinfo) {
            wifimanager.disableNetwork(wifiinfo.getNetworkId());
        }
        boolean result = false;
        if (config.networkId > 0) {
            result = wifimanager.enableNetwork(config.networkId, true);
            wifimanager.updateNetwork(config);
        } else {
            int i = wifimanager.addNetwork(config);
            if (i > 0) {
                wifimanager.saveConfiguration();
                return wifimanager.enableNetwork(i, true);
            }
        }
        return result;
    }

    /**
     * 查看以前是否也配置过这个网络
     *
     * @param SSID    wifi名称
     * @param context .
     * @return 是否存在这个wifi配置
     */
    public static WifiConfiguration isExist(String SSID, Context context) {
        WifiManager wifimanager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> existingConfigs = wifimanager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }


    /**
     * @param SSID    wifi名称
     * @param context .
     * @return 连接状态
     */
    public static String wifiConnectStatus(String SSID, Context context) {
        String status = "未保存";
        boolean isExist = isExist(SSID, context) != null;
        if (isExist) {
            status = "已保存";
        }
//        WifiInfo connectedWifiInfo = getConnectedWifiInfo(context);
//        if (connectedWifiInfo != null && TextUtils.equals("\"" + SSID + "\"", connectedWifiInfo.getSSID())) {
//            status = "已连接";
//        }
        return status;
    }

    /**
     * @param SSID    wifi名称
     * @param context .
     * @return 连接状态
     */
    public static String wifiConnectStatus2(String SSID, Context context) {
        String status = "未保存";
        boolean isExist = isExist(SSID, context) != null;
        if (isExist) {
            status = "已保存";
        }
        WifiInfo connectedWifiInfo = getConnectedWifiInfo(context);
        if (connectedWifiInfo != null && TextUtils.equals("\"" + SSID + "\"", connectedWifiInfo.getSSID())
                && WifiUtil.isExistWifiNetWork(context)) {
            status = "已连接";
        }
        return status;
    }


    /**
     * @param context 。
     * @return 是否有wifi网络
     */
    public static boolean isExistWifiNetWork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                MLog.d("isExistWifiNetWork(WifiUtil.java:160)wifi 网络连接");
                return true;
            }
        }
        MLog.d("isExistWifiNetWork(WifiUtil.java:166)网络未连接");
        return false;
    }

    /**
     * 返回level 等级
     */
    public static int getLevel(int level) {
        if (Math.abs(level) < 50) {
            return 1;
        } else if (Math.abs(level) < 75) {
            return 2;
        } else if (Math.abs(level) < 90) {
            return 3;
        } else {
            return 4;
        }
    }

    /**
     * @param context .
     * @return 获取连接成功的wifi信息
     */
    public static WifiInfo getConnectedWifiInfo(Context context) {
        return ((WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
    }

}
