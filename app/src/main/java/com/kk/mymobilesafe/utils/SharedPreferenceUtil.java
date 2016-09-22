package com.kk.mymobilesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 一个 SharedPreferences 的简化工具类
 * Created by Administrator on 2016/9/20.
 */
public class SharedPreferenceUtil {
    /**
     * SharedPreferences 的配置名
     */
    public static final String CONFIG = "config";

    /**
     * @param context 上下文
     * @param key
     * @param value   保存的boolean 值
     * @return ture 保存成功 false 保存失败
     */
    public static boolean putBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    /**
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }

    /**
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static boolean putString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    /**
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

}
