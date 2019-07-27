package com.study.bluetooth.adapter;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
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
public class BluetoothHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView bluetoothTypeIv;
    public TextView bluetoothName;
    private List<BluetoothDevice> list;
    private IItemListener<BluetoothDevice> listener;

    public BluetoothHolder(@NonNull View itemView, List<BluetoothDevice> list, IItemListener<BluetoothDevice> listener) {
        super(itemView);
        bluetoothTypeIv = itemView.findViewById(R.id.bluetoothTypeIv);
        bluetoothName = itemView.findViewById(R.id.bluetoothName);
        this.list = list;
        this.listener = listener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onItemClick(getAdapterPosition(), list.get(getAdapterPosition()));
        }
    }
}
