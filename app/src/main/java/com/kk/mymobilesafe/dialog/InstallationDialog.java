package com.kk.mymobilesafe.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.kk.mymobilesafe.activity.HomeActivity;
import com.kk.mymobilesafe.signle.MySignal;

/**
 * Created by Administrator on 2016/9/19.
 */
public class InstallationDialog {
    Activity activity;
    Handler handler;

    /**
     *
     * @param activity  界面的Acitvity
     * @param handler   界面里的Handler
     */
    public InstallationDialog(Activity activity, Handler handler) {
        this.activity = activity;
        this.handler=handler;
    }

    /**
     *  弹出询问是否安装的对话框
     */
     public void askInstall(){
         AlertDialog.Builder builder=new AlertDialog.Builder(activity);
         builder.setTitle("安装更新");
         builder.setMessage("是否现在更新？");
         builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 Message message=Message.obtain();
                 message.what= MySignal.Installation.YES;
                 handler.sendMessage(message);
             }
         });
         builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 Message message=Message.obtain();
                 message.what= MySignal.Installation.NO;
                 handler.sendMessage(message);
             }
         });
         builder.create().show();
     }
}
