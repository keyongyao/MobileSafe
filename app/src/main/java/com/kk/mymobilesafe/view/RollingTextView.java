package com.kk.mymobilesafe.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * 自定义滚动的TextView
 * Created by Administrator on 2016/9/20.
 */
public class RollingTextView extends TextView {
    public RollingTextView(Context context) {
        // 调用下面的构造器
        this(context, null);
    }

    public RollingTextView(Context context, AttributeSet attrs) {
        // 调用下面的构造器
        this(context, attrs, 0);
    }

    public RollingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setSingleLine();
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setMarqueeRepeatLimit(-1);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
    }

}
