package com.sendtion.poteviodemo.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sendtion.poteviodemo.R;

/**
 * 启动页
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
