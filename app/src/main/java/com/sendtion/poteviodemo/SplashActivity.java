package com.sendtion.poteviodemo;

import android.content.Intent;
import android.os.Bundle;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * 启动页
 */
public class SplashActivity extends RxAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
