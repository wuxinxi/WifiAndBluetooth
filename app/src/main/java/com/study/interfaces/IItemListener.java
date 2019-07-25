package com.study.interfaces;

/**
 * 作者：Tangren on 2019-07-25
 * 包名：com.study.interfaces
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */
public interface IItemListener<T> {
    void onItemClick(int position,T t);
}
