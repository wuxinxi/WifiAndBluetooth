package com.study.bluetooth.listener;

/**
 * 作者：Tangren on 2019-07-27
 * 包名：com.study.bluetooth.listener
 * 邮箱：996489865@qq.com
 * TODO:蓝牙开关状态的回调接口
 */
public interface OnBluetoothStateListener {
    /**
     * 正在关闭
     */
    void onBluetoothStateClosing();

    /**
     * 蓝牙断开
     */
    void onBluetoothStateColsed();

    /**
     * 正在打开
     */
    void onBluetoothStateOpening();

    /**
     * 已打开
     */
    void onBluetoothStateOpened();
}
