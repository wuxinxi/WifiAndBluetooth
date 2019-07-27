package com.study.bluetooth.manage;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.study.bluetooth.listener.OnBluetoothStateListener;
import com.study.bluetooth.listener.OnDiscoveryDeviceListener;
import com.study.util.MLog;

import java.util.Locale;
import java.util.Set;

/**
 * 作者：Tangren on 2019-07-27
 * 包名：com.study.bluetooth.manage
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */
public class BluetoothManager {

    private final BluetoothAdapter bluetoothAdapter;

    public BluetoothManager() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * 打开蓝牙
     */
    public void openBluetooth() {
        if (bluetoothAdapter != null) {
            bluetoothAdapter.enable();
        }
    }

    public void closeBluetooth() {
        if (bluetoothAdapter != null) {
            bluetoothAdapter.disable();
        }
    }

    /**
     * @return 返回蓝牙的名字
     */
    public String getName() {
        if (bluetoothAdapter != null) {
            return bluetoothAdapter.getName();
        }
        return null;
    }

    /**
     * 设置蓝牙名字
     *
     * @param name 名字
     * @return 是否成功
     */
    public boolean setName(String name) {
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        return bluetoothAdapter != null && bluetoothAdapter.setName(name);
    }

    /**
     * @return 返回已绑定的设备
     */
    public Set<BluetoothDevice> getBounderDevices() {
        if (bluetoothAdapter != null) {
            return bluetoothAdapter.getBondedDevices();
        }
        return null;
    }

    /**
     * 扫描附近可用的蓝牙设备
     *
     * @return 是否正常开启扫描
     */
    public boolean discovery() {
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isDiscovering()) {
                return bluetoothAdapter.cancelDiscovery() && bluetoothAdapter.startDiscovery();
            } else {
                return bluetoothAdapter.startDiscovery();
            }
        }
        return false;
    }

    /**
     * @return 是否正在扫描
     */
    public boolean isDiscovering() {
        return bluetoothAdapter != null && bluetoothAdapter.isDiscovering();
    }

    /**
     * @return 蓝牙是否打开
     */
    public boolean isEnabled() {
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    private static OnDiscoveryDeviceListener onDiscoveryDeviceListener;

    private static OnBluetoothStateListener onBluetoothStateListener;

    public  void setOnDiscoveryDeviceListener(OnDiscoveryDeviceListener onDiscoveryDeviceListener) {
        BluetoothManager.onDiscoveryDeviceListener = onDiscoveryDeviceListener;
    }

    public  void setOnBluetoothStateListener(OnBluetoothStateListener onBluetoothStateListener) {
        BluetoothManager.onBluetoothStateListener = onBluetoothStateListener;
    }

    public void removeOnDiscoveryDeviceListener() {
        if (onDiscoveryDeviceListener != null) {
            onDiscoveryDeviceListener = null;
        }
    }

    public void removeOnBluetoothStateListener() {
        if (onBluetoothStateListener != null) {
            onBluetoothStateListener = null;
        }
    }

    /**
     * 检索蓝牙设备的广播
     */
    public static class SearchBluetoothDeviceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                return;
            }
            assert action != null;
            switch (action) {
                case BluetoothDevice.ACTION_FOUND:
                    //发现设备
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    MLog.d("onReceive(SearchBluetoothDeviceReceiver.java:116)" + String.format(Locale.CHINA, "发现新设备： %s MAC地址：%s", device.getName(), device.getAddress()));
                    if (onDiscoveryDeviceListener != null) {
                        onDiscoveryDeviceListener.onDiscoverDeviceFound(device);
                    }
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    MLog.d("onReceive(SearchBluetoothDeviceReceiver.java:126)开始检索附近设备");
                    if (onBluetoothStateListener != null) {
                        onDiscoveryDeviceListener.onDiscoveryDeviceStarted();
                    }
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    MLog.d("onReceive(SearchBluetoothDeviceReceiver.java:148)检索设备结束");
                    if (onDiscoveryDeviceListener != null) {
                        onDiscoveryDeviceListener.onDiscoverDeviceComplete();
                    }
                    break;
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    MLog.d("onReceive(SearchBluetoothDeviceReceiver.java:166)蓝牙状态发送改变");
                    int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
                    switch (state) {
                        case BluetoothAdapter.STATE_TURNING_ON:
                            MLog.d("onReceive(SearchBluetoothDeviceReceiver.java:170)正的打开蓝牙");
                            if (onBluetoothStateListener != null) {
                                onBluetoothStateListener.onBluetoothStateOpening();
                            }
                            break;
                        case BluetoothAdapter.STATE_ON:
                            MLog.d("onReceive(SearchBluetoothDeviceReceiver.java:175)蓝牙已打开");
                            if (onBluetoothStateListener != null) {
                                onBluetoothStateListener.onBluetoothStateOpened();
                            }
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            MLog.d("onReceive(SearchBluetoothDeviceReceiver.java:180)蓝牙真正关闭");
                            if (onBluetoothStateListener != null) {
                                onBluetoothStateListener.onBluetoothStateClosing();
                            }
                            break;
                        case BluetoothAdapter.STATE_OFF:
                            MLog.d("onReceive(SearchBluetoothDeviceReceiver.java:188)蓝牙已关闭");
                            if (onBluetoothStateListener != null) {
                                onBluetoothStateListener.onBluetoothStateColsed();
                            }
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    }

}
