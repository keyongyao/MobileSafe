package com.kk.mymobilesafe.bean;

import android.graphics.drawable.Drawable;

/**
 * 保存进程信息，主要被进程模块使用
 * Created by Administrator on 2016/9/29.
 */

public class ProcessInfoBean {
    public String name;
    public Drawable icon;
    public long memSize;
    public boolean isCheck;
    public boolean isSystem;
    public String pkgName;

}
