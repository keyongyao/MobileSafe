package com.kk.mymobilesafe.handler;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.kk.mymobilesafe.activity.HomeActivity;
import com.kk.mymobilesafe.bean.VersionBean;
import com.kk.mymobilesafe.dialog.InstallationDialog;
import com.kk.mymobilesafe.dialog.UpdateDialog;
import com.kk.mymobilesafe.signle.MySignal;
import com.kk.mymobilesafe.utils.LogCatUtil;
import com.kk.mymobilesafe.utils.VersionUtil;

import java.io.File;

/**
 *
 * Created by Administrator on 2016/9/20.
 */
public class HandlerLuancher extends Handler {
    private static final String TAG = "main";
    Activity mActivity;
    Handler mHandler;
    VersionBean versionBean;
    /**
     * 新的安装包
     */
    File saveFile;

    public HandlerLuancher(Activity mActivity) {
        this.mActivity = mActivity;
        mHandler=this;
    }

    public HandlerLuancher(Callback callback, Activity mActivity) {
        super(callback);
        this.mActivity = mActivity;
    }

    public HandlerLuancher(Looper looper, Activity mActivity) {
        super(looper);
        this.mActivity = mActivity;
    }

    public HandlerLuancher(Looper looper, Callback callback, Activity mActivity) {
        super(looper, callback);
        this.mActivity = mActivity;
    }

    @Override

    public void handleMessage(Message msg) {
        switch (msg.what){

            // 有更新版本
            case MySignal.Update.HAS_NEW_VERSION_YES:{
                versionBean = (VersionBean) msg.obj;
                LogCatUtil.getSingleton().i(TAG, versionBean.toString());
                new UpdateDialog(mActivity, versionBean, mHandler).askUpdate();

                break;
            }
            // 没更新版本呢
            case MySignal.Update.HAS_NEW_VERSION_NO:{
                Toast.makeText(mActivity.getApplicationContext(), "已经是最新版本", Toast.LENGTH_SHORT).show();
                goToHomeActivity();
                break;
            }
            // 检查版本出错
            case MySignal.Update.HAS_NEW_VERSION_ERROR:{
                Toast.makeText(mActivity.getApplicationContext(), "连接服务器失败", Toast.LENGTH_SHORT).show();
                goToHomeActivity();
                break;
            }
            // 用户点击确定更新按钮
            case MySignal.Update.YES:{
                Toast.makeText(mActivity, "正在更新...", Toast.LENGTH_SHORT).show();
                String fileName=versionBean.downloadUrl.substring(versionBean.downloadUrl.lastIndexOf("/"),
                        versionBean.downloadUrl.length());
                File file=new File(Environment.getExternalStorageDirectory(),fileName);
                new VersionUtil().downloadAPK(versionBean, file, mHandler);
                // 不能 关闭当前 Activity

                break;
            }
            // 用户点击取消跟新按钮
            case MySignal.Update.NO:{
                // 如果用户取消更新 则进入 HomeActivity 并且关闭当前 Actiivty
                Toast.makeText(mActivity, "已取消更新", Toast.LENGTH_SHORT).show();
                goToHomeActivity();
                break;
            }
            // 下载新的安装包成功
            case MySignal.Download.SUCCEED:{
                Toast.makeText(mActivity, "下载跟新包成功", Toast.LENGTH_SHORT).show();
                saveFile = (File)msg.obj;
                new InstallationDialog(mActivity,mHandler).askInstall();
                break;
            }
            // 下载新的安装包失败
            case MySignal.Download.FAILURE:{
                Toast.makeText(mActivity, "下载跟新包失败", Toast.LENGTH_SHORT).show();
                goToHomeActivity();
                break;

            }
            case MySignal.Installation.YES:{
                Toast.makeText(mActivity, "用户安装新报", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setDataAndType(Uri.fromFile(saveFile),"application/vnd.android.package-archive");
                mActivity.startActivity(intent);
                // 只有关闭当前的Activity 安装后才能跳转到新的APP
                mActivity.finish();
                break;
            }
            case MySignal.Installation.NO:{
                goToHomeActivity();
                break;
            }
        }
    }
   // 转到 HomeActivity ，并且销毁当前的 LauncherActivity
    private void goToHomeActivity() {
        Intent intent=new Intent();
        intent.setClass(mActivity.getApplicationContext(), HomeActivity.class);
        mActivity.startActivity(intent);
        mActivity.finish();
    }
}
