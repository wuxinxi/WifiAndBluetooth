package com.study.wifi.entity;

/**
 * 作者：Tangren on 2019-07-25
 * 包名：com.study.entity
 * 邮箱：996489865@qq.com
 * TODO:Scanresult->wifiResult
 */
public class WifiResult implements Comparable<WifiResult> {

    //wifi名称
    public String ssid;
    //信号强度
    public int level;
    //已连接  正在连接  未连接
    public String state;
    //加密方式
    public String capabilities;

    public WifiResult(String ssid, int level, String state, String capabilities) {
        this.ssid = ssid;
        this.level = level;
        this.state = state;
        this.capabilities = capabilities;
    }

    @Override
    public int compareTo(WifiResult o) {

        return o.level - this.level;
    }
}
