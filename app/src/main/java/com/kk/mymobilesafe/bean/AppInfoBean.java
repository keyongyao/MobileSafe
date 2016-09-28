package com.kk.mymobilesafe.bean;

import android.graphics.drawable.Drawable;

/**
 * 封装App信息
 * Created by Administrator on 2016/9/27.
 */

public class AppInfoBean {
    public String name;
    public String pkgName;
    public Drawable icon;
    public boolean isUserApp;
    public boolean isRom;

    @Override
    public String toString() {
        return "AppInfoBean{" +
                "name='" + name + '\'' +
                ", pkgName='" + pkgName + '\'' +
                ", icon=" + icon +
                ", isUserApp=" + isUserApp +
                ", isRom=" + isRom +
                '}';
    }
}
