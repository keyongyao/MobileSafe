package com.kk.rocket;

import android.app.Service;
import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

/**
 * 获取屏幕的物理像素点 <br>
 * Created by Administrator on 2016/9/25.
 */

public class ScreenSizeUtil {
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Service.WINDOW_SERVICE);
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point.x;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Service.WINDOW_SERVICE);
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point.y;
    }
}
