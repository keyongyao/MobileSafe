package com.kk.mymobilesafe.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kk.mymobilesafe.bean.BlackNumBean;
import com.kk.mymobilesafe.utils.LogCatUtil;

import java.util.Random;

/**
 * 操作黑名单数据库
 * Created by Administrator on 2016/9/26.
 */

public class BlackNumDao {
    private static final String TAG = "main";
    Context mContext;
    SQLiteOpenHelper helper;

    public BlackNumDao(Context mContext) {
        this.mContext = mContext;
        helper = new SQLiteOpenHelperBlackNum(mContext);
    }

    public void initData() {
        clearDB();
        for (int i = 10; i < 99; i++) {
            BlackNumBean bean = new BlackNumBean();
            bean.phone = "131786609" + i;
            Random random = new Random();
            bean.blockType = random.nextInt(3) + 1;
            insert(bean);
        }
    }

    public boolean clearDB() {
        SQLiteDatabase database = helper.getWritableDatabase();
        int delete = database.delete("main", null, null); //清空数据
        ContentValues values = new ContentValues();
        values.put("seq", 0);
        database.update("sqlite_sequence", values, "name=?", new String[]{"main"}); //自增长ID为0
        if (delete > 0)
            return true;
        return false;
    }

    public Cursor queryAll() {
        SQLiteDatabase database = helper.getWritableDatabase();
        Cursor cursor = database.query("main", new String[]{"_id", "phone", "blockTypeID"}, null, null, null, null, " _id desc");
        //   database.close();
        return cursor;
    }

    public long insert(BlackNumBean bean) {
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("phone", bean.phone);
        values.put("blockTypeID", bean.blockType);
        long insert = database.insert("main", null, values);
        LogCatUtil.getSingleton().i(TAG, "插入黑名单一个数据");
        return insert;
    }

    public int delete(BlackNumBean bean) {
        SQLiteDatabase database = helper.getReadableDatabase();
        int del = database.delete("main", "phone=?", new String[]{bean.phone});
        LogCatUtil.getSingleton().i(TAG, "删除黑名单一个数据");
        return del;

    }

    public int update(BlackNumBean bean) {
        SQLiteDatabase database = helper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("phone", bean.phone);
        values.put("blockTypeID", bean.blockType);
        int update = database.update("main", values, "_id=?", new String[]{bean._id + ""});
        LogCatUtil.getSingleton().i(TAG, "更新黑名单一个数据");
        return update;
    }

    public Cursor paged(int count) {
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.query("main", new String[]{"_id", "phone", "blockTypeID"}, null, null, null, null, " _id desc", count + ",15");
        return cursor;
    }

    public boolean isInBlackNumList(String phoneNum) {
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.query("main", new String[]{"phone", "blockTypeID"}, "phone=? and (blockTypeID=1 or blockTypeID=3)",
                new String[]{phoneNum}, null, null, null);
        if (cursor.getCount() > 0) {
            return true;
        }
        return false;
    }

}


class SQLiteOpenHelperBlackNum extends SQLiteOpenHelper {
    private static final String TAG = "main";

    public SQLiteOpenHelperBlackNum(Context context) {

        super(context, "blacknum.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tableMain = "create table main(_id integer primary key AUTOINCREMENT not null, phone vachar(11) not null , blockTypeID char(1) not null);";
        String tableType = "create table type( blockTypeID Integer   primary key AUTOINCREMENT, type varchar(6) not null);";
        String initTypedata = "insert into type(type) values('短信'),('来电'),('所有');";
        db.execSQL(tableMain);
        db.execSQL(tableType);
        db.execSQL(initTypedata);
        LogCatUtil.getSingleton().i(TAG, "黑名单数据库创建成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}