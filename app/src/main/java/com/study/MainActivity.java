package com.study;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;

import com.study.base.BaseActivity;
import com.study.bluetooth.BluetoothActivity;
import com.study.file.listener.OnFileProgressListener;
import com.study.file.util.FileThread;
import com.study.util.MLog;
import com.study.wifi.WifiMainActivity;

import java.io.File;


public class MainActivity extends BaseActivity {


    @Override
    protected int layoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Button wifi = findViewById(R.id.wifi);
        Button bluetooth = findViewById(R.id.bluetooth);
        Button file = findViewById(R.id.file);

        wifi.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, WifiMainActivity.class)));
        bluetooth.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, BluetoothActivity.class)));

        file.setOnClickListener(v -> {
            String localPath = Environment.getExternalStorageDirectory() + File.separator + "test/NewProject.zip";
            String sdPath = "/storage/sdcard1/test";
            FileThread thread = new FileThread(localPath, sdPath);
            thread.setOnFileProgressListener(new OnFileProgressListener() {
                @Override
                public void onSuccess() {
                    MLog.d("onSuccess(MainActivity.java:42)copy成功·");
                }

                @Override
                public void onFail( Exception e) {
                    MLog.e(e);
                }

                @Override
                public void onProgress(String fileName, int progress) {
                    MLog.d("onProgress(MainActivity.java:51)filename:"+fileName+",progress="+progress);
                }

            });
            thread.start();
        });

    }

    @Override
    protected void initData() {

    }

}
