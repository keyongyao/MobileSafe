package com.kk.mymobilesafe.bean;

/**
 * 黑名单bean
 * Created by Administrator on 2016/9/26.
 */

public class BlackNumBean {
    public int _id;
    public String phone;
    public int blockType;

    @Override
    public String toString() {
        return "BlackNumBean{" +
                "_id=" + _id +
                ", phone='" + phone + '\'' +
                ", blockType=" + blockType +
                '}';
    }
}
