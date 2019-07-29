package com.study.file.listener;

/**
 * 作者：Tangren on 2019-07-29
 * 包名：com.study.file.listener
 * 邮箱：996489865@qq.com
 * TODO:文件导入导出进度监听
 */
public interface OnFileProgressListener {
    /**
     * 成功
     */
    void onSuccess();

    /**
     * 失败
     *
     * @param e        异常信息
     */
    void onFail( Exception e);

    /**
     * 进度
     *
     * @param fileName 文件名
     * @param progress 进度
     */
    void onProgress(String fileName, int progress);
}
