package com.kk.mymobilesafe.bean;

/**
 * 保存版本信息的VerisonBean <br>
 * Created by Administrator on 2016/9/19.
 */
public class VersionBean {
    public String versionName;
    public String versionCode;
    public String description;
    public String downloadUrl;

    @Override
    public String toString() {
        return "VersionBean{" +
                "versionName='" + versionName + '\'' +
                ", versionCode='" + versionCode + '\'' +
                ", description='" + description + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                '}';
    }
}
