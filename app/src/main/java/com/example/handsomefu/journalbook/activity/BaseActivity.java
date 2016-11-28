package com.example.handsomefu.journalbook.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by HandsomeFu on 2016/11/2.
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        initView();
        initData();
        initEvent();
    }
    protected abstract void initEvent();
    protected abstract void initData();
    protected abstract void initView();
    public abstract int getLayout();
}
