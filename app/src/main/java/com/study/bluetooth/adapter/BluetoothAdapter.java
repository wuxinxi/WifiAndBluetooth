package com.study.bluetooth.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.study.R;
import com.study.listener.IItemListener;

import java.util.List;

/**
 * 作者：Tangren on 2019-07-27
 * 包名：com.study.bluetooth.adapter
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */
public class BluetoothAdapter extends RecyclerView.Adapter<BluetoothHolder> {
    private List<BluetoothDevice> list;
    private Context context;
    private LayoutInflater inflater;
    private IItemListener<BluetoothDevice> listener;

    public BluetoothAdapter(List<BluetoothDevice> list,Context context) {
        this.list=list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void refreshDatas(List<BluetoothDevice> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addDatas(BluetoothDevice device) {
        if (list!=null){
            list.add(device);
            notifyDataSetChanged();
        }

    }

    public void clear() {
        if (list != null && !list.isEmpty()) {
            list.clear();
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BluetoothHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.view_bluetooth_item, viewGroup, false);
        return new BluetoothHolder(view, list, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull BluetoothHolder bluetoothHolder, int position) {
        bluetoothHolder.bluetoothName.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public void setListener(IItemListener<BluetoothDevice> listener) {
        this.listener = listener;
    }
}
