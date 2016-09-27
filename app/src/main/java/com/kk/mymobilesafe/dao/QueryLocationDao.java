package com.kk.mymobilesafe.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.kk.mymobilesafe.signle.MySignal;

/**
 * Created by Administrator on 2016/9/24.
 */

public class QueryLocationDao {
    /**
     * @param context        在那个activity 执行的查询
     * @param incommingPhone 要查询的号码
     * @param handler        发送处理查询结果
     */
    public static void query(final Context context, final String incommingPhone, final Handler handler) {
        new Thread() {
            @Override
            public void run() {
                String location = "";
                String outkey = null;
                String path = context.getFilesDir().getPath() + "/" + "address.db";
                Message message = Message.obtain();
                SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);

                if (incommingPhone.startsWith("0")) {
                    Cursor cursor = database.query("data2", new String[]{"location"}, " area=?", new String[]{incommingPhone.substring(1)}, null, null, null);
                    while (cursor.moveToNext()) {
                        location = location + "\n" + cursor.getString(0);
                    }
                }
                if (incommingPhone.length() >= 7) {

                    Cursor cursor = database.query("data1", new String[]{"outkey"}, "id=?",
                            new String[]{incommingPhone.substring(0, 7)}, null, null,
                            null);
                    while (cursor.moveToNext()) {
                        outkey = cursor.getString(0);
                        Cursor cursor1 = database.query("data2", new String[]{"location"}, "id=?", new String[]{outkey}, null, null, null);
                        while (cursor1.moveToNext()) {
                            location = cursor1.getString(0);
                        }
                    }
                }

                if (incommingPhone.length() < 7) {
                    switch (incommingPhone) {
                        case "110":
                            message.what = MySignal.ATool.QUERYRESULT;
                            message.obj = "报警";
                            handler.sendMessage(message);
                            return;

                        case "120":
                            message.what = MySignal.ATool.QUERYRESULT;
                            message.obj = "急救";
                            handler.sendMessage(message);
                            return;

                        case "119":
                            message.what = MySignal.ATool.QUERYRESULT;
                            message.obj = "火警";
                            handler.sendMessage(message);
                            return;
                    }
                }
                if (TextUtils.isEmpty(location)) {
                    message.what = MySignal.ATool.QUERYRESULT;
                    message.obj = "未知地区";
                    handler.sendMessage(message);

                } else {
                    // 发送结果
                    message.what = MySignal.ATool.QUERYRESULT;
                    message.obj = location;
                    handler.sendMessage(message);
                }
            }
        }.start();

    }
}
