package com.kk.mymobilesafe.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;

import com.kk.mymobilesafe.R;
import com.kk.mymobilesafe.utils.LogCat;

/**
 * 从短信获取手机指令
 * Created by Administrator on 2016/9/23.
 */

public class RecevieSMS extends BroadcastReceiver {
    private static final String TAG = "main";

    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] pdus = (Object[]) intent.getExtras().get("pdus");
        String messageBody = null;

        for (Object oo : pdus
                ) {
            SmsMessage sms = SmsMessage.createFromPdu((byte[]) oo);
            //5,获取短信对象的基本信息
            String originatingAddress = sms.getOriginatingAddress();
            messageBody = sms.getMessageBody();
            LogCat.getSingleton().i(TAG, "RecevieSMS.onReceive: 收到短信了" + originatingAddress + "  " + messageBody);
        }

        if (messageBody.contains("#*alarm*#")) {
            MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
            //  mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
        if (messageBody.contains("#*location*#")) {
            // TODO: 2016/9/23  return location 
        }
        if (messageBody.contains("#*locakscreen*#")) {
            // TODO: 2016/9/23  locak screen 
        }
        if (messageBody.contains("#*wipedata*#")) {
            // TODO: 2016/9/23 wipe data
        }
    }
}
