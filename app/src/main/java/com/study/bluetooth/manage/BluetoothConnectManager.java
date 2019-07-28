package com.study.bluetooth.manage;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

import com.study.bluetooth.listener.OnConnectionListener;
import com.study.util.MLog;

import java.io.IOException;
import java.util.UUID;

/**
 * 作者：Tangren on 2019-07-27
 * 包名：com.study.bluetooth.manage
 * 邮箱：996489865@qq.com
 * TODO:蓝牙连接管理
 */
public class BluetoothConnectManager {
    public static final int NOTHING = -1;
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
    private static UUID UUID_SECURE = UUID.fromString("f291a307-6ee4-4bec-8f1a-1a1bdb8b3844");
    private static UUID UUID_INSECURE = UUID.fromString("dae8ce34-29de-499f-bc12-be586ccf3881");

    private static ConnectThread mConnectThread;

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
                        connectionListener.onConnectSuccess(device);
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


    /**
     * 初始化蓝牙连接
     */
    public synchronized void init() {
        // 取消之前的连接线程
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        connectState = NOTHING;
    }


    /**
     * 连接蓝牙设备
     *
     * @param device 蓝牙设备
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    public synchronized void connect(BluetoothDevice device, boolean secure) {
        MLog.d("正在连接：" + device.getName());
        // 取消之前的连接线程
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // 客户端发起连接请求
        mConnectThread = new ConnectThread(device, secure);
        mConnectThread.start();

        connectState = CONNECTING;
    }


    private class ConnectThread extends Thread {
        private BluetoothDevice device;
        private BluetoothSocket socket;

        public ConnectThread(BluetoothDevice device, boolean secure) {
            BluetoothSocket tempSocket = null;
            try {
                this.device = device;
                if (secure) {
                    //安全连接
                    if (secure) {
                        tempSocket = device.createRfcommSocketToServiceRecord(UUID_SECURE);
                    } else {
                        tempSocket = device.createInsecureRfcommSocketToServiceRecord(UUID_INSECURE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                socket = tempSocket;
            }

        }

        @Override
        public void run() {
            super.run();
            try {
                // 正在连接...
                changeConnectState(CONNECTING, null);
                // 开始连接 阻塞线程 连接成功继续执行 连接失败抛异常
                socket.connect();
            } catch (IOException e) {
                e.printStackTrace();
                // 连接失败
                changeConnectState(CONNECT_FAIL, e);
                try {
                    socket.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                return;
            }

            // 线程执行完置空重置
            synchronized (BluetoothConnectManager.this) {
                mConnectThread = null;
            }
        }

        void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 改变蓝牙连接状态
     *
     * @param state  状态
     * @param object 传递的数据
     */
    private void changeConnectState(int state, Object object) {
        Message message = Message.obtain();
        message.what = state;
        message.obj = object;
        handler.sendMessage(message);
    }


    public void setConnectionListener(OnConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
    }
}
