package com.kk.mymobilesafe.utils;

import android.util.Log;

/**
 * 包装 android.util.Log 类 做成全局开关模式 使用静态内部，保证单例
 * Created by Administrator on 2016/9/19.
 */
public  final class LogCat {
    private LogCat(){ }
    private static boolean log_open=false;

    private static class Hodler{
        private static final LogCat instance=new LogCat();
    }
    public  static final LogCat getSingleton(){
        return Hodler.instance;
    }

    public  boolean isLog_open() {
        return log_open;
    }

    public  void setLog_open(boolean log_open) {
        LogCat.log_open = log_open;
    }

    public  void v(String tag, String msg){
      if (log_open){
          Log.v(tag,msg);
      }
    }
    public  void d(String tag,String msg){
        if (log_open){
            Log.d(tag, msg);
        }

    }
    public void i(String tag,String msg){
        if (log_open){
            Log.i(tag, msg);
        }

    }
    public  void w(String tag ,String msg,Exception exception){
        if (log_open){
            Log.w(tag,msg, exception);

        }

    }
    public  void e(String tag ,String msg,Exception exception){
        if (log_open){
            Log.e(tag,msg,exception );
        }

    }

}
