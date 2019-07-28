package com.study.bluetooth.adapter;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.study.R;
import com.study.listener.IItemListener;

import java.util.List;

/**
 * 作者：Tangren on 2019-07-27
 * 包名：com.study.bluetooth.adapter
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */
public class GroupBluetoothHolder extends BluetoothHolder {
    public TextView type;

    public GroupBluetoothHolder(@NonNull View itemView, List<BluetoothDevice> list, IItemListener<BluetoothDevice> listener) {
        super(itemView, list, listener);
        type = itemView.findViewById(R.id.type);
    }

}
