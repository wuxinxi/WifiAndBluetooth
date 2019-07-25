package com.study.adapter;

import android.support.v7.util.DiffUtil;
import android.text.TextUtils;

import com.study.entity.WifiResult;

import java.util.List;

/**
 * 作者：Tangren on 2019-07-25
 * 包名：com.study.adapter
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */
public class WifiDiffCallBack extends DiffUtil.Callback {
    private List<WifiResult> mOldWifiResults, mNewWifiResults;

    public WifiDiffCallBack(List<WifiResult> mOldWifiResults, List<WifiResult> mNewWifiResults) {
        this.mOldWifiResults = mOldWifiResults;
        this.mNewWifiResults = mNewWifiResults;
    }

    @Override
    public int getOldListSize() {
        return mOldWifiResults != null ? mOldWifiResults.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return mNewWifiResults != null ? mNewWifiResults.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldPosition, int newPosition) {
        if (mOldWifiResults.size() - 1 < oldPosition || mNewWifiResults.size() - 1 < newPosition) {
            return false;
        }
        if (mOldWifiResults.get(oldPosition) == null && mNewWifiResults.get(newPosition) != null) {
            return false;
        }
        if (mOldWifiResults.get(oldPosition) != null && mNewWifiResults.get(newPosition) == null) {
            return false;
        }
        if (mOldWifiResults.get(oldPosition) == null && mNewWifiResults.get(newPosition) == null) {
            return true;
        }
        return TextUtils.equals(mOldWifiResults.get(oldPosition).state, mNewWifiResults.get(newPosition).state);
    }

    @Override
    public boolean areContentsTheSame(int oldPosition, int newPosition) {
        if (mOldWifiResults.size() - 1 < oldPosition || mNewWifiResults.size() - 1 < newPosition) {
            return false;
        }
        return TextUtils.equals(mOldWifiResults.get(oldPosition).state, mNewWifiResults.get(newPosition).state);
    }
}
