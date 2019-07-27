package com.study.bluetooth.manage;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.Message;

import com.study.bluetooth.listener.OnConnectionListener;
import com.study.util.MLog;

import java.util.UUID;

/**
 * 作者：Tangren on 2019-07-27
 * 包名：com.study.bluetooth.manage
 * 邮箱：996489865@qq.com
 * TODO:蓝牙连接管理
 */
public class BluetoothConnectManager {
    //开始连接
    public static final int CONNECTING = 0;
    //连接成功
    public static final int CONNECT_SUCCESS = 1;
    //连接失败
    public static final int CONNECT_FAIL = 2;
    //连接丢失
    public static final int CONNECT_LOST = 3;

    private int connectState = 0;

    private final BluetoothAdapter bluetoothAdapter;

    private OnConnectionListener connectionListener;
    private UUID UUID_SECURE;
    private UUID UUID_INSECURE;

    public BluetoothConnectManager() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @SuppressLint("HandlerLeak")
    //内存泄露
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CONNECTING:
                    MLog.d("handleMessage(BluetoothConnectManager.java:42)开始连接蓝牙");
                    if (connectionListener != null) {
                        connectionListener.onConnecting();
                    }
                    break;
                case CONNECT_SUCCESS:
                    MLog.d("handleMessage(BluetoothConnectManager.java:49)连接成功");
                    if (connectionListener != null) {
                        BluetoothDevice device = (BluetoothDevice) msg.obj;
                        connectionListener.onConnetSuccess(device);
                    }
                    break;
                case CONNECT_FAIL:
                    MLog.d("handleMessage(BluetoothConnectManager.java:57)连接失败");
                    if (connectionListener != null) {
                        Exception e = (Exception) msg.obj;
                        connectionListener.onConnectFail(e);
                    }
                case CONNECT_LOST:
                    MLog.d("handleMessage(BluetoothConnectManager.java:63)连接丢失");
                    if (connectionListener != null) {
                        Exception e = (Exception) msg.obj;
                        connectionListener.onConnectLost(e);
                    }
                    break;
                default:
                    break;
            }
            connectState = msg.what;
        }
    };

    public synchronized int getState() {
        return connectState;
    }

    private class ConnectThread extends Thread {
        private BluetoothDevice device;

        public ConnectThread(BluetoothDevice device, boolean secure) {
            try {
                this.device = device;
                if (secure) {
                    //安全连接

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {
            super.run();
        }
    }
}
