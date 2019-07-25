package com.study.base;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

/**
 * 作者：Tangren on 2019-07-25
 * 包名：com.study.base
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */
public abstract class BaseActivity extends AppCompatActivity {
    public static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    public static final int PERMISSION_RES_CODE = 0x100;

    protected abstract int layoutID();

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void initData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID());
        initView(savedInstanceState);
        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requstPermission();
        }
    }

    private void requstPermission() {
        int checkPermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission_group.LOCATION);
        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_RES_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_RES_CODE) {
            if (grantResults.length <= 0) {
                return;
            }
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!shouldShowRequestPermissionRationale(permissions[0])) {
                        //勾选了再不提醒
                        //提示用户授予权限->去设置中给予权限
                        noPermission();
                    } else {
                        //授权被拒绝，并且勾选了不再提醒
                        noPermission();
                    }
                }
            }
        }
    }


    private void noPermission() {
        new AlertDialog.Builder(this)
                .setTitle("权限不可用")
                .setMessage("请在-应用设置-权限-中，允许APP使用相关权限.")
                .setPositiveButton("开启", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, PERMISSION_RES_CODE);
                }).setNegativeButton("不,谢谢", (dialog, which) -> finish()).setCancelable(false).show();
    }
}
