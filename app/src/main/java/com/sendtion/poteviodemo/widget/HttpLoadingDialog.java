package com.sendtion.poteviodemo.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sendtion.poteviodemo.R;

public class HttpLoadingDialog extends Dialog {

    private TextView contentView;
    private FourCircleRotate fourCircleRotate;

    public HttpLoadingDialog(Context context, String msg) {
        super(context, R.style.http_loading_dialog);
        View view = initView(context, msg);
        setCancelable(true);
        setContentView(view, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
    }

    private View initView(Context context, String msg) {
        int layoutId = R.layout.comm_http_loading_dialog;
        View layout = LayoutInflater.from(context).inflate(layoutId, null);
        contentView = layout.findViewById(R.id.comm_http_loading_dialog_content);
        fourCircleRotate = layout.findViewById(R.id.comm_http_loading_dialog_animation);
        contentView.setText(msg);
        return layout;
    }

    public void setMsg(String msg) {
        if (contentView != null) {
            contentView.setText(msg);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            fourCircleRotate.startAnimation();
        } else {
            fourCircleRotate.stop();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (contentView != null) {
            dismiss();
        }
    }
}
