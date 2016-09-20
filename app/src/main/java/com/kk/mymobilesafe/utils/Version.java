package com.kk.mymobilesafe.utils;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;

import com.kk.mymobilesafe.bean.VersionBean;
import com.kk.mymobilesafe.signle.MySignal;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 版本的检测，下载新版本
 * Created by Administrator on 2016/9/19.
 */
public class Version {
    Activity activity;
    Handler mHandler;
    private static final String TAG = "main";

    /**
     * 适用除downloadAPK方法以外的方法是用
     * @param mHandler
     * @param activity
     */
    public Version(Handler mHandler,Activity activity) {
        this.mHandler = mHandler;
        this.activity=activity;
    }

    /**
     *  适用 downloadAPK 方法
     */
    public Version(){

    }


    /**
     *
     * @return  返回版本码， -1：获取不到版本码
     */
    public  int getLocalVersionCode(){
        PackageManager packageManager = activity.getApplicationContext().getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(activity.getApplicationContext().getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     *
     * @return 返回包的版本信息 android studio版本信息在 Module app 的 build.gradle配置
     */
    public  String getLocalVersionName(){
        PackageManager packageManager = activity.getApplicationContext().getPackageManager();
        try {

            PackageInfo packageInfo = packageManager.getPackageInfo(activity.getApplicationContext().getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     *
     * @param serverPath  含有服务器的版本信息的JSON文件路径
     */
    public void checkVersion(final String serverPath) {

            new Thread() {

                @Override
                public void run() {
                    ByteArrayOutputStream outputStream=null;
                    InputStream inputStream = null;
                    VersionBean versionBean = new VersionBean();
                    URL url = null;
                    try {
                        url = new URL(serverPath);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setReadTimeout(3*1000);
                        connection.setConnectTimeout(3*1000);
                        if(connection.getResponseCode()==200){

                            inputStream = connection.getInputStream();
                            outputStream = new ByteArrayOutputStream();
                            int len = 0;
                            byte[] flush = new byte[2048];
                            while (-1 != (len = inputStream.read(flush))) {
                                outputStream.write(flush, 0, len);
                            }
                            outputStream.flush();
                            JSONObject jsonObject = new JSONObject(outputStream.toString());
                            versionBean.versionName = jsonObject.getString("versionName");
                            versionBean.description = jsonObject.getString("description");
                            versionBean.versionCode = jsonObject.getString("versionCode");
                            versionBean.downloadUrl = jsonObject.getString("downloadUrl");

                            Thread.sleep(2000);
                            // 返回结果信息 给 Handler
                            Message msg=Message.obtain();
                            int lastestVersonCode=Integer.parseInt(versionBean.versionCode);
                            if (getLocalVersionCode()<lastestVersonCode){
                                msg.what=MySignal.Update.HAS_NEW_VERSION_YES;
                                msg.obj=versionBean;
                                mHandler.sendMessage(msg);
                            }else {
                                msg.what=MySignal.Update.HAS_NEW_VERSION_NO;
                                msg.obj=versionBean;
                                mHandler.sendMessage(msg);
                            }

                        }
                        if (connection.getResponseCode()==404){
                            Message msg=Message.obtain();
                            msg.what=MySignal.Update.HAS_NEW_VERSION_ERROR;
                            mHandler.sendMessage(msg);
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        Message msg=Message.obtain();
                        msg.what=MySignal.Update.HAS_NEW_VERSION_ERROR;
                        mHandler.sendMessage(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Message msg=Message.obtain();
                        msg.what=MySignal.Update.HAS_NEW_VERSION_ERROR;
                        mHandler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Message msg=Message.obtain();
                        msg.what=MySignal.Update.HAS_NEW_VERSION_ERROR;
                        mHandler.sendMessage(msg);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            Thread.sleep(4000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        IOsUtil.closeALL(outputStream,inputStream);
                    }


                }
            }.start();
    }

    /**
     *
     * @param versionBean  通过checkVersion 获取 VersionBean对象
     * @param saveFile   保存的文件位置
     */
    public  void downloadAPK(VersionBean versionBean, final File saveFile, final Handler handler) {
        HttpUtils httpUtils=new HttpUtils();
        httpUtils.download(versionBean.downloadUrl, saveFile.getAbsolutePath(), new RequestCallBack<File>() {
            LogCat logCat=LogCat.getSingleton();
            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
              logCat.e(TAG,"下载成功",null);
                Message message=Message.obtain();
                message.what= MySignal.Download.SUCCEED;
                message.obj=saveFile;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Message message=Message.obtain();
                message.what= MySignal.Download.FAILURE;
                handler.sendMessage(message);
                logCat.i(TAG,"下载失败");
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                logCat.i(TAG,"正在下载");
                super.onLoading(total, current, isUploading);
            }

            @Override
            public void onStart() {
                logCat.i(TAG,"开始下载");
                super.onStart();
            }
        });
    }
}
