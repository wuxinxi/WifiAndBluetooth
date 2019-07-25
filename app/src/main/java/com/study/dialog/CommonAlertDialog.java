package com.study.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.study.R;
import com.study.interfaces.IDialogListener;

/**
 * 作者：Tangren on 2019-07-04
 * 包名：com.szxb.base.dialog
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */
public class CommonAlertDialog extends Dialog implements View.OnClickListener {
    private LayoutInflater inflater;
    private IDialogListener listener;
    private EditText content;
    private String currentUrl;

    public CommonAlertDialog(Context context) {
        super(context, R.style.MyDialogStyle);
        inflater = LayoutInflater.from(context);
    }

    public CommonAlertDialog(Context context, String defaultUrl) {
        super(context, R.style.MyDialogStyle);
        this.currentUrl = defaultUrl;
        inflater = LayoutInflater.from(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.view_psw_dialog, null);
        content = view.findViewById(R.id.content);
        TextView cancel = view.findViewById(R.id.cancel);
        TextView confirm = view.findViewById(R.id.confirm);
        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);
        if (currentUrl != null) {
            content.setText(currentUrl);
        }
        setContentView(view);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.cancel) {
            cancel();
        } else if (i == R.id.confirm) {
            if (listener != null) {
                listener.onConfirm(content.getText().toString());
                cancel();
            }
        }
    }

    public CommonAlertDialog setOnDialogListener(IDialogListener listener) {
        this.listener = listener;
        return this;
    }

}
