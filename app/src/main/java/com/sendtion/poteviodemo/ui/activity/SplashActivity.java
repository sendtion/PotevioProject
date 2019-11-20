package com.sendtion.poteviodemo.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.badoo.mobile.util.WeakHandler;
import com.sendtion.poteviodemo.util.StatusBarUtils;

/**
 * 启动页
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setBackgroundDrawable(null);
        //setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        StatusBarUtils.setFullScreen(this);

        new WeakHandler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 300);
    }
}
