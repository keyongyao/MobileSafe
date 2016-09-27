package com.kk.mymobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kk.mymobilesafe.R;
import com.kk.mymobilesafe.constant.Constant;
import com.kk.mymobilesafe.utils.ScreenSizeUtil;
import com.kk.mymobilesafe.utils.SharedPreferenceUtil;

public class SetPositionActivity extends Activity {
    ImageView mIVlocation;
    Button btnToptip, btnbottomTip;
    long doubleclick[] = new long[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_position);
        initUi();
        setLinstener();
    }

    private void setLinstener() {
        // 设置控件的双击事件
        mIVlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.arraycopy(doubleclick, 1, doubleclick, 0, doubleclick.length - 1);
                doubleclick[doubleclick.length - 1] = SystemClock.uptimeMillis();
                // 检测为双击事件 就重置控件的位置
                if (doubleclick[doubleclick.length - 1] - doubleclick[0] < 500) {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );
                    layoutParams.leftMargin = ScreenSizeUtil.getScreenWidth(getApplicationContext()) / 2 - mIVlocation.getWidth() / 2;
                    layoutParams.topMargin = ScreenSizeUtil.getScreenHeight(getApplicationContext()) / 2 - mIVlocation.getHeight() / 2;
                    mIVlocation.setLayoutParams(layoutParams);
                }
            }
        });
        mIVlocation.setOnTouchListener(new View.OnTouchListener() {
            // 事件的起始坐标
            int startY;
            int startX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        // 点击时 的坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        // 移动后的坐标
                        int moveX = (int) event.getRawX();
                        int moveY = (int) event.getRawY();
                        // 实际要移动的坐标差
                        int disX = moveX - startX;
                        int disY = moveY - startY;

                        // 控件应该移动到的位置
                        int left = mIVlocation.getLeft() + disX;
                        int right = mIVlocation.getRight() + disX;
                        int top = mIVlocation.getTop() + disY;
                        int bottom = mIVlocation.getBottom() + disY;

                        // 设置 上下提示 是否显示
                        if (top > ScreenSizeUtil.getScreenHeight(getApplicationContext()) / 2) {
                            btnbottomTip.setVisibility(View.INVISIBLE);
                            btnToptip.setVisibility(View.VISIBLE);
                        } else {
                            btnbottomTip.setVisibility(View.VISIBLE);
                            btnToptip.setVisibility(View.INVISIBLE);
                        }
                        // 不让控件跑到屏幕外边去 返回 不更新
                        int screenX = ScreenSizeUtil.getScreenWidth(getApplicationContext());
                        int screenY = ScreenSizeUtil.getScreenHeight(getApplicationContext());

                        if (left < 0 || top < 0 || bottom > screenY || right > screenX) {
                            return true;
                        }

                        // 设置 控件的位置
                        mIVlocation.layout(left, top, right, bottom);

                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        // 保存 控件的当前位置
                        SharedPreferenceUtil.putInt(getApplicationContext(), Constant.SettingCenter.LOCATIONX, mIVlocation.getLeft());
                        SharedPreferenceUtil.putInt(getApplicationContext(), Constant.SettingCenter.LOCATIONY, mIVlocation.getTop());
                        break;
                    }
                }
                return false;
            }

        });
    }

    private void initUi() {
        mIVlocation = (ImageView) findViewById(R.id.iv_settingCenter_position);

        btnToptip = (Button) findViewById(R.id.btn_settingCenter_setposition_top_tip);
        btnbottomTip = (Button) findViewById(R.id.btn_settingCenter_setposition_bottom_tip);
        // 读取配置 初始化控件位置
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.leftMargin = SharedPreferenceUtil.getInt(getApplicationContext(), Constant.SettingCenter.LOCATIONX);
        layoutParams.topMargin = SharedPreferenceUtil.getInt(getApplicationContext(), Constant.SettingCenter.LOCATIONY);
        mIVlocation.setLayoutParams(layoutParams);

        // 设置 上下提示 是否显示
        if (layoutParams.topMargin > ScreenSizeUtil.getScreenHeight(getApplicationContext()) / 2) {
            btnbottomTip.setVisibility(View.INVISIBLE);
            btnToptip.setVisibility(View.VISIBLE);
        } else {
            btnbottomTip.setVisibility(View.VISIBLE);
            btnToptip.setVisibility(View.INVISIBLE);
        }


    }
}
