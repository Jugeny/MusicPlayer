package com.example.administrator.mymusicapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.administrator.mymusicapp.utils.AppConfigUtils;

public class WelcomeActivity extends AppCompatActivity {
    Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        handler.postDelayed(runnable,2000);
    }
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            //判断是否第一次使用  使用SharedPreferences 存在是否第一次使用
            boolean guide= AppConfigUtils.getInstance().getGuide(WelcomeActivity.this);
            if (guide) {
                startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));
                finish();
            }else {
                startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
            }
        }
    };

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }
}
