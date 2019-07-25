package com.study.util;

/**
 * 作者：Tangren on 2019-07-25
 * 包名：com.study.util
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */
public class Constant {

    /**
     * 本地广播action
     */
    public static final String LOCAL_ACTION = "com.me.local.receiver";

    /**
     * 本地广播FLAG
     */
    public static final String LOCAL_RECEIVER_FLAG = "LOCAL_RECEIVER_FLAG";


    /**
     * wifi 没有连接上
     */
    public static final int WIFI_STATE_DISCONNECTED = -1;

    /**
     * wifi 连接成功
     */
    public static final int WIFI_STATE_CONNECTED = WIFI_STATE_DISCONNECTED + 1;

    /**
     * wifi正在连接
     */
    public static final int WIFI_STATE_CONNECTING = WIFI_STATE_CONNECTED + 1;

    /**
     * 重新扫描
     */
    public static final int SCAN_RESULTS_AVAILABLE_ACTION = WIFI_STATE_CONNECTING + 1;

}
