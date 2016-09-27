package com.kk.rocket;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

public class RocketDispatchActivity extends Activity {
    Handler mHandler;
    Activity mActivity;
    ImageView botttom, top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rocket_dispatch);
        initUI();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
            }
        };
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1500);
    }

    private void initUI() {
        botttom = (ImageView) findViewById(R.id.iv_rocktet_bottom);
        top = (ImageView) findViewById(R.id.iv_rocket_top);
        AlphaAnimation animation = new AlphaAnimation(1, 0);
        animation.setDuration(1500);
        animation.setFillAfter(true);
        botttom.startAnimation(animation);
        top.startAnimation(animation);
    }
}
