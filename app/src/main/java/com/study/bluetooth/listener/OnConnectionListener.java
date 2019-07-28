package com.study.bluetooth.listener;

import android.bluetooth.BluetoothDevice;

/**
 * 作者：Tangren on 2019-07-27
 * 包名：com.study.bluetooth.listener
 * 邮箱：996489865@qq.com
 * TODO:连接的回调
 */
public interface OnConnectionListener {
    /**
     * 配对中
     *
     * @param device 设备信息
     */
    void onPairing(BluetoothDevice device);

    /**
     * 配对成功
     *
     * @param device 设备信息
     */
    void onPairSuccess(BluetoothDevice device);

    /**
     * 配对失败
     *
     * @param e 失败原因
     */
    void onPairFail(BluetoothDevice device, Exception e);


    /**
     * 正在连接
     */
    void onConnecting();

    /**
     * 连接成功
     *
     * @param device 设备信息
     */
    void onConnectSuccess(BluetoothDevice device);

    /**
     * 连接失败
     *
     * @param e 失败原因
     */
    void onConnectFail(Exception e);

    /**
     * 连接中断
     *
     * @param e 中断原因
     */
    void onConnectLost(Exception e);

}
