package com.example.administrator.mymusicapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.mymusicapp.R;
import com.example.administrator.mymusicapp.activity.base.BaseBottomNavActivity;

public class TestActivity extends BaseBottomNavActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }
}
