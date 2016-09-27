package com.kk.rocket;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * 启动火箭的
 * Created by Administrator on 2016/9/25.
 */

public class RocketService extends Service {
    WindowManager wm;
    View rocketView;
    ImageView imageView;
    Handler mHandler;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        wm = (WindowManager) getSystemService(Service.WINDOW_SERVICE);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.setTitle("Toast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //              | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        rocketView = View.inflate(getApplicationContext(), R.layout.layout_rocket, null);
        // 火箭的ImageView
        imageView = (ImageView) rocketView.findViewById(R.id.iv_rocket);
        AnimationDrawable ani = (AnimationDrawable) imageView.getBackground();
        ani.start();
        params.gravity = Gravity.TOP + Gravity.LEFT;
        wm.addView(rocketView, params);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                params.y = msg.what;
                wm.updateViewLayout(rocketView, params);
            }
        };
        rocketView.setOnTouchListener(new View.OnTouchListener() {
            int startX;
            int startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        int moveX = (int) event.getRawX();
                        int moveY = (int) event.getRawY();

                        int disX = moveX - startX;
                        int disY = moveY - startY;

                        params.x = params.x + disX;
                        params.y = params.y + disY;


                        wm.updateViewLayout(rocketView, params);

                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();

                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        int X = ScreenSizeUtil.getScreenWidth(getApplicationContext());
                        int Y = ScreenSizeUtil.getScreenHeight(getApplicationContext());
                        if (params.x > X / 2 - 150 && params.x < X / 2 + 150 && params.y > Y - 200) {
                            Log.i("main", "RocketService.onTouch: X-Y:" + params.x + " " + params.y + "  X/2=" + X / 2);
                            dispatchRocket();
                        }
                        break;
                    }
                }
                return true;
            }
        });

    }

    private void dispatchRocket() {
        final int Y = ScreenSizeUtil.getScreenHeight(getApplicationContext());
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < Y / 5; i++) {
                            int height = Y - i * 5;
                            SystemClock.sleep(2);
                            Message message = Message.obtain();
                            message.what = height;
                            mHandler.sendMessage(message);
                        }
                    }
                }
        ).start();

        Intent intent = new Intent(getApplicationContext(), RocketDispatchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        wm.removeView(rocketView);
    }
}
