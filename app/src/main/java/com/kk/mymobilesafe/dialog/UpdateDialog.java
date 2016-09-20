package com.kk.mymobilesafe.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

import com.kk.mymobilesafe.bean.VersionBean;
import com.kk.mymobilesafe.signle.MySignal;

/**
 * Created by Administrator on 2016/9/19.
 */
public class UpdateDialog {
    Activity mActivity;
    VersionBean versionBean;
    Handler handler;

    /**
     *
     * @param mActivity  打开Dialog的Activity
     * @param versionBean 版本信息，填充Dialog的窗体
     * @param handler   处理用户的选择
     */
    public UpdateDialog(Activity mActivity, VersionBean versionBean ,Handler handler) {
        this.versionBean=versionBean;
        this.mActivity = mActivity;
        this.handler=handler;
    }

    /**
     *   弹出询问用户是否进行升级的对话框
     */
    public void askUpdate(){
        AlertDialog.Builder builder=new AlertDialog.Builder(mActivity);
        builder.setTitle("发现新版本");
        builder.setMessage(versionBean.description);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Message message=Message.obtain();
                message.what= MySignal.Update.NO;
                handler.sendMessage(message);
            }
        });
        builder.setPositiveButton("升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Message message=Message.obtain();
                message.what= MySignal.Update.YES;
                handler.sendMessage(message);
            }
        });
        builder.create().show();
    }
}
