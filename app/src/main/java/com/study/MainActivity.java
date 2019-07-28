package com.study;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.study.base.BaseActivity;
import com.study.bluetooth.BluetoothActivity;
import com.study.wifi.WifiMainActivity;


public class MainActivity extends BaseActivity{


    @Override
    protected int layoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Button wifi=findViewById(R.id.wifi);
        Button bluetooth=findViewById(R.id.bluetooth);

        wifi.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, WifiMainActivity.class)));
        bluetooth.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, BluetoothActivity.class)));
    }

    @Override
    protected void initData() {

    }

}
