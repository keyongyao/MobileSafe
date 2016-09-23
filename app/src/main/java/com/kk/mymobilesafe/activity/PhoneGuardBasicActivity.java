package com.kk.mymobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;


public abstract class PhoneGuardBasicActivity extends Activity {
    GestureDetector detector;

    /**
     * 子类实现上一页的跳转
     */
    abstract void nextPage();

    /**
     * 子类实现下一页的跳转
     */
    abstract void prevousPage();

    @Override
    /**
     *  改变触摸事件的返回值
     */
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                /**
                 * 下一页
                 */
                if (e1.getX() - e2.getX() > 100) {
                    nextPage();
                }
                /**
                 * 上一页
                 */
                if (e2.getX() - e1.getX() > 100) {
                    prevousPage();
                }

                return super.onFling(e1, e2, velocityX, velocityY);

            }
        });
    }
}
