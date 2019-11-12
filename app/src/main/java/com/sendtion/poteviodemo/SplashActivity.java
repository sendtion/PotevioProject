package com.sendtion.poteviodemo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 启动页
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setTitle("闪屏页");
        setContentView(R.layout.activity_splash);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(view ->
                startActivity(new Intent(SplashActivity.this,MainActivity.class)));
    }
}
