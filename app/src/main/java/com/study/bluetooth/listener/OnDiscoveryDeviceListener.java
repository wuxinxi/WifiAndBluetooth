package com.study.bluetooth.listener;

import android.bluetooth.BluetoothDevice;

/**
 * 作者：Tangren on 2019-07-27
 * 包名：com.study.bluetooth.listener
 * 邮箱：996489865@qq.com
 * TODO:检索监听
 */
public interface OnDiscoveryDeviceListener {
    /**
     * 开始检索
     */
    void onDiscoveryDeviceStarted();

    /**
     * 发现蓝牙
     *
     * @param device 设备信息
     */
    void onDiscoverDeviceFound(BluetoothDevice device);

    /**
     * 检索完成
     */
    void onDiscoverDeviceComplete();
}
