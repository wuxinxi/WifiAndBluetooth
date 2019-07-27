package com.study.wifi.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.study.R;
import com.study.wifi.entity.WifiResult;
import com.study.listener.IItemListener;

import java.util.List;

/**
 * 作者：Tangren on 2019-07-25
 * 包名：com.study.adapter
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */
public class WifiViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView wifiName, wifiSaveStatus;
    public ImageView wifiSignalIv;
    private List<WifiResult> list;
    private IItemListener<WifiResult> listener;

    public WifiViewHolder(@NonNull View itemView, List<WifiResult> list, IItemListener<WifiResult> listener) {
        super(itemView);
        this.list = list;
        this.listener = listener;
        wifiName = itemView.findViewById(R.id.wifiName);
        wifiSaveStatus = itemView.findViewById(R.id.wifiSaveStatus);
        wifiSignalIv = itemView.findViewById(R.id.wifiSignalIv);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onItemClick(getAdapterPosition(), list.get(getAdapterPosition()));
        }
    }
}
