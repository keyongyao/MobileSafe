package com.kk.mymobilesafe.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

import com.kk.mymobilesafe.dao.BlackNumDao;
import com.kk.mymobilesafe.utils.LogCatUtil;

/**
 * Created by Administrator on 2016/9/26.
 */

public class BlockSMSRecevier extends BroadcastReceiver {
    private static final String TAG = "main";

    // TODO: 2016/9/27  好像有序广播改为无序广播了  服务接收不正常
    @Override
    public void onReceive(Context context, Intent intent) {
        LogCatUtil.getSingleton().i(TAG, "收到短信了");

        Object[] pdus = (Object[]) intent.getExtras().get("pdus");
        String originatingAddress = "";
        for (Object oo : pdus
                ) {
            SmsMessage message = SmsMessage.createFromPdu((byte[]) oo);
            originatingAddress = message.getOriginatingAddress();
        }
        boolean isOnList = new BlackNumDao(context).isInBlackNumList(originatingAddress);
        LogCatUtil.getSingleton().i(TAG, "是否在黑名单中" + isOnList + "");
        if (isOnList)
            abortBroadcast();
        LogCatUtil.getSingleton().i(TAG, "停止了广播");

    }
}
