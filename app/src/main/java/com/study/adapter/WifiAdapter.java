package com.study.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.study.R;
import com.study.entity.WifiResult;
import com.study.interfaces.IItemListener;

import java.util.List;

/**
 * 作者：Tangren on 2019-07-25
 * 包名：com.study.adapter
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */
public class WifiAdapter extends RecyclerView.Adapter<WifiViewHolder> {
    private List<WifiResult> list;
    private LayoutInflater inflater;
    private IItemListener<WifiResult> listener;
    private Context context;

    public WifiAdapter(List<WifiResult> list, Context mContext) {
        this.list = list;
        this.context = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    public void refreshDatas(List<WifiResult> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void clear() {
        this.list.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WifiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.view_wifi_item, viewGroup, false);
        return new WifiViewHolder(view, list, listener);
    }

//    @Override
//    public void onBindViewHolder(@NonNull WifiViewHolder wifiViewHolder, int i, @NonNull List<Object> payloads) {
//        if (payloads.isEmpty()) {
//            String ssid = list.get(i).ssid;
//            wifiViewHolder.wifiName.setText(ssid);
//            wifiViewHolder.wifiSaveStatus.setText(list.get(i).state);
//        } else {
//            int type = (int) payloads.get(0);
//            if (type == 0) {
//                //只更新状态
//                wifiViewHolder.wifiSaveStatus.setText(list.get(i).state);
//            }
//        }
//    }

    @Override
    public void onBindViewHolder(@NonNull WifiViewHolder wifiViewHolder, int i) {
        String ssid = list.get(i).ssid;
        wifiViewHolder.wifiName.setText(ssid);
        wifiViewHolder.wifiSaveStatus.setText(list.get(i).state);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setListener(IItemListener<WifiResult> listener) {
        this.listener = listener;
    }
}
