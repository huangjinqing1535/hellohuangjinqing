package com.huang.database.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by huang on 12/29/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DB_NAME = "getui_db";
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         * 联系人
         */
        String sql_contants = "Create table IF NOT EXISTS "
                + DBcolumns.TABLE_CONTANT + "(" + DBcolumns.CONTANT_ID + " integer primary key AUTOINCREMENT,"
                + DBcolumns.CONTANT_NAME + " text,"
                + DBcolumns.CONTANT_CLIENTID + " text,"
                + DBcolumns.CONTANT_GROUP_NAME + " text,"
                + DBcolumns.CONTANT_STATE + " text);";
        /**
         * 聊天记录
         */
        String sql_msg = "Create table IF NOT EXISTS " + DBcolumns.TABLE_MSG
                + "(" + DBcolumns.MSG_ID + " integer primary key autoincrement,"
                + DBcolumns.MSG_NAME + " text,"
                + DBcolumns.MSG_FROM + " text,"
                + DBcolumns.MSG_TO + " text,"
                + DBcolumns.MSG_TYPE + " text,"
                + DBcolumns.MSG_CONTENT + " text,"
                + DBcolumns.MSG_DATE + " text,"
                + DBcolumns.MSG_ISREADED + " text,"
                + DBcolumns.MSG_TO_CLIENTID + " text,"
                + DBcolumns.MSG_FROM_CLIENTID + " text);";

        /**
         * 聊天列表
         */
        String sql_msg_list = "Create table IF NOT EXISTS " + DBcolumns.TABLE_MSGLIST
                + "(" + DBcolumns.LIST_ID + " integer primary key autoincrement,"
                + DBcolumns.LIST_NAME + " text,"
                + DBcolumns.LIST_FROM + " text,"
                + DBcolumns.LIST_TO + " text,"
                + DBcolumns.LIST_TYPE + " text,"
                + DBcolumns.LIST_CONTENT + " text,"
                + DBcolumns.LIST_DATE + " text,"
                + DBcolumns.LIST_ISREADED + " text,"
                + DBcolumns.LIST_TO_CLIENTID + " text,"
                + DBcolumns.LIST_FROM_CLIENTID + " text);";

        db.execSQL(sql_contants);
        db.execSQL(sql_msg);
        db.execSQL(sql_msg_list);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
