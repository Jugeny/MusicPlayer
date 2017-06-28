package com.example.administrator.mymusicapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.administrator.mymusicapp.R;
import com.example.administrator.mymusicapp.activity.base.BaseBottomNavActivity;

public class NearFriendActivity extends BaseBottomNavActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局
        setContentView(R.layout.activity_near_friend);
    }

    /**
     * 跳转测试界面
     * @param view
     */
    public void startTest(View view){
        Intent intent=new Intent(this,TestActivity.class);
        startActivity(intent);
    }
}
