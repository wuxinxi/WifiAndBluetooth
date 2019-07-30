package com.study.file.util;

import com.study.file.listener.OnFileProgressListener;
import com.study.util.MLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 作者：Tangren on 2019-07-29
 * 包名：com.study.file.util
 * 邮箱：996489865@qq.com
 * TODO:文件操作
 */
public class FileThread extends Thread {

    private OnFileProgressListener onFileProgressListener;
    private String sourcePath;
    private String newPath;
    private boolean isOk = true;

    /**
     * @param sourcePath 源路径
     * @param newPath    copy的路径
     */
    public FileThread(String sourcePath, String newPath) {
        this.sourcePath = sourcePath;
        this.newPath = newPath;
    }

    @Override
    public void run() {
        super.run();
        if (new File(sourcePath).isFile()) {
            copyAppointFile(sourcePath, newPath);
        } else if (new File(sourcePath).isDirectory()) {
            copyFolder(sourcePath, newPath);
        } else {
            if (onFileProgressListener != null) {
                onFileProgressListener.onFail(new IllegalArgumentException("无效参数"));
            }
            return;
        }
        if (onFileProgressListener != null && isOk) {
            onFileProgressListener.onSuccess();
        }
    }


    /**
     * 复制具体文件到指定文件夹
     *
     * @param sourceFilePath   源文件完整路径
     * @param targetPathFolder 目标完整文件路径
     */
    private void copyAppointFile(String sourceFilePath, String targetPathFolder) {
        File sourceFile = new File(sourceFilePath);
        if (!sourceFile.exists() || !sourceFile.isFile()) {
            if (onFileProgressListener != null) {
                onFileProgressListener.onFail(new FileNotFoundException("源文件不存在"));
                return;
            }
        }

        File targetFile = new File(targetPathFolder);
        if (!targetFile.exists()) {
            boolean mkdirs = targetFile.mkdirs();
        }
        try {
            copy(sourceFilePath, targetPathFolder, sourceFile);

        } catch (Exception e) {
            MLog.e(e);
            isOk = false;
            if (onFileProgressListener != null) {
                onFileProgressListener.onFail(e);
            }
        }
    }


    /**
     * 复制整个文件夹内容
     *
     * @param oldPath String 原文件路径
     * @param newPath String 复制后路径
     */
    private void copyFolder(String oldPath, String newPath) {
        File oldFile = new File(oldPath);
        String[] file = oldFile.list();
        if (file == null || file.length == 0) {
            if (onFileProgressListener != null) {
                onFileProgressListener.onFail(new FileNotFoundException("目标文件不存在"));
            }
            return;
        }
        File targetFile = new File(newPath);
        if (!targetFile.exists()) {
            boolean mkdirs = targetFile.mkdirs();
        }
        File temp = null;
        try {
            for (String aFile : file) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + aFile);
                } else {
                    temp = new File(oldPath + File.separator + aFile);
                }
                if (temp.isFile()) {
                    copy(oldPath + File.separator + temp.getName(), newPath, temp);
                }
                if (temp.isDirectory()) {
                    //如果是子文件夹
                    copyFolder(oldPath + File.separator + aFile, newPath + File.separator + aFile);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            MLog.e(e);
            if (onFileProgressListener != null) {
                onFileProgressListener.onFail(e);
            }
            isOk = false;
        }

    }


    /**
     * copy
     *
     * @param sourceFilePath   源文件
     * @param targetPathFolder 目标路径
     * @param sourceFile       .
     * @throws IOException .
     */
    private void copy(String sourceFilePath, String targetPathFolder, File sourceFile) throws IOException {
        FileInputStream input = new FileInputStream(sourceFile);
        String fileName = sourceFilePath.substring(sourceFilePath.lastIndexOf(File.separator) + 1, sourceFilePath.length());
        FileOutputStream output = new FileOutputStream(targetPathFolder + File.separator + fileName);
        int fileSize = input.available();
        byte[] data = new byte[1024];
        int len;
        int process;
        long currentSize = 0;
        int tempProcess = 0;
        while ((len = input.read(data)) != -1) {
            output.write(data, 0, len);
            currentSize = currentSize + len;
            process = (int) ((((float) currentSize / fileSize)) * 100);
            if (tempProcess != process) {
                tempProcess = process;
                if (onFileProgressListener != null) {
                    onFileProgressListener.onProgress(fileName, process);
                }
            }
        }
        output.flush();
        output.close();
        input.close();
    }

    public void setOnFileProgressListener(OnFileProgressListener onFileProgressListener) {
        this.onFileProgressListener = onFileProgressListener;
    }
}
