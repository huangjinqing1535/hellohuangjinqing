package com.huang.database.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.huang.entity.Constact;
import com.huang.entity.MsgContent;
import com.huang.utils.Flog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huang on 12/29/15.
 * 数据库处理类
 */
public class DBDao {

    private static final String TAG = "DBDao";

    private DatabaseHelper mDatabaseHelper = null;

    public static DBDao mDBDao;

    public static DBDao getInstance(Context context){
        if (mDBDao==null){
            mDBDao = new DBDao(context);
        }
        return mDBDao;
    }

    public  DBDao(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
    }

    /**
     * 插入一个新的联系人
     * @param mContant
     * @return
     */
    public void insertConstact(Constact mContant) {
        if (queryContantName(mContant)){
            updateConstact(mContant);
        }else {
            SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DBcolumns.CONTANT_NAME, mContant.getName());
            values.put(DBcolumns.CONTANT_CLIENTID, mContant.getClientid());
            values.put(DBcolumns.CONTANT_GROUP_NAME, mContant.getGroupName());
            values.put(DBcolumns.CONTANT_STATE, mContant.getState());
            db.insert(DBcolumns.TABLE_CONTANT, null, values);
            db.close();
        }

    }


    /**
     * 更新constact
     * @param mContant
     */
    public void updateConstact(Constact mContant){
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        String sql = "UPDATE table_contant SET contant_clientid = ?,contant_state = ?,contant_groupname=? WHERE contant_name = ?";
        Object[] datas = new Object[]{new String(mContant.getClientid()),new String(mContant.getState()),new String(mContant.getGroupName()),new String(mContant.getName())};
        db.execSQL(sql, datas);
        db.close();
    }



    /**
     * 查询联系人是否存在
     *
     * @return
     */
    public boolean queryContantName(Constact mConstact) {
        boolean isExist = false;
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        String sql = "select * from " + DBcolumns.TABLE_CONTANT+" where contant_name = ?";
        String[] tName = new String[] { mConstact.getName() };
        Cursor mCursor = db.rawQuery(sql, tName);
        while (mCursor.moveToNext()){
            isExist = true;
        }
        return isExist;
    }

    /**
     * 查询联系人是否存在
     *
     * @return
     */
    public boolean queryContantByName(String name) {
        boolean isExist = false;
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        String sql = "select * from " + DBcolumns.TABLE_CONTANT+" where contant_name = ?";
        String[] tName = new String[] { name };
        Cursor mCursor = db.rawQuery(sql, tName);
        while (mCursor.moveToNext()){
            isExist = true;
        }
        return isExist;
    }


    /**
     * 查询所有的组名
     *
     * @return
     */
    public List<String> queryAllConstactGroupName() {
        List<String> groupNames = new ArrayList<String>();
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        String sql = "select distinct " + DBcolumns.CONTANT_GROUP_NAME+" from "+DBcolumns.TABLE_CONTANT;

        Cursor mCursor = db.rawQuery(sql, null);
        while (mCursor.moveToNext()){
           String groupName = mCursor.getString(0);
            groupNames.add(groupName);
        }
        return groupNames;
    }


    /**
     * 查询所有的联系人
     *
     * @return
     */
    public List<Constact> queryAllContants() {
        List<Constact> mContants= new ArrayList<Constact>();
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        String sql = "select * from " + DBcolumns.TABLE_CONTANT;
        Cursor mCursor = db.rawQuery(sql, null);
        while (mCursor.moveToNext()){
            String name = mCursor.getString(mCursor.getColumnIndex(DBcolumns.CONTANT_NAME));
            String clientid = mCursor.getString(mCursor.getColumnIndex(DBcolumns.CONTANT_CLIENTID));
            String groupName = mCursor.getString(mCursor.getColumnIndex(DBcolumns.CONTANT_GROUP_NAME));
            String state = mCursor.getString(mCursor.getColumnIndex(DBcolumns.CONTANT_STATE));
            Constact mContant = new Constact(name,clientid,groupName,state);
            Flog.e(TAG,mContant.toString());
            mContants.add(mContant);
        }
        return mContants;
    }

    /**
     * 根据组名查询所有的联系人
     *
     * @return
     */
    public List<Constact> queryContantsByGroupName(String groupName) {
        List<Constact> mContants= new ArrayList<Constact>();
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        String sql = "select * from " + DBcolumns.TABLE_CONTANT+" where contant_groupname=?";
        String[] tName = new String[] { groupName };
        Cursor mCursor = db.rawQuery(sql, tName);
        while (mCursor.moveToNext()){
            String name = mCursor.getString(mCursor.getColumnIndex(DBcolumns.CONTANT_NAME));
            String clientid = mCursor.getString(mCursor.getColumnIndex(DBcolumns.CONTANT_CLIENTID));
            String group = mCursor.getString(mCursor.getColumnIndex(DBcolumns.CONTANT_GROUP_NAME));
            String state = mCursor.getString(mCursor.getColumnIndex(DBcolumns.CONTANT_STATE));
            Constact mContant = new Constact(name,clientid,group,state);
            Flog.e(TAG,mContant.toString());
            mContants.add(mContant);
        }
        return mContants;
    }



    /**
     * 插入一条msgcontent
     * @param msgContent
     * @return
     */
    public int insertMessage(MsgContent msgContent) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBcolumns.MSG_FROM, msgContent.getFromName());
        values.put(DBcolumns.MSG_TO, msgContent.getToName());
        values.put(DBcolumns.MSG_NAME, msgContent.getMsgName());
        values.put(DBcolumns.MSG_TYPE, msgContent.getType());
        values.put(DBcolumns.MSG_CONTENT, msgContent.getContent());
        values.put(DBcolumns.MSG_DATE, msgContent.getDate());
        values.put(DBcolumns.MSG_ISREADED, msgContent.getIsRead());
        values.put(DBcolumns.MSG_FROM_CLIENTID, msgContent.getFromClientid());
        values.put(DBcolumns.MSG_TO_CLIENTID, msgContent.getToClientid());
        db.insert(DBcolumns.TABLE_MSG, null, values);
        db.close();
        insertNewMsgList(msgContent);
        int msgid = queryTheLastMsgId();//返回新插入记录的id
        return msgid;

    }
    /**
     * 查询最新一条记录的id
     *
     * @return
     */
    public int queryTheLastMsgId() {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        String sql = "select " + DBcolumns.MSG_ID + " from " + DBcolumns.TABLE_MSG + " order by " + DBcolumns.MSG_ID + " desc limit 1";
        String[] args = new String[]{};
        Cursor cursor = db.rawQuery(sql, args);
        int id = -1;
        if (cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndex(DBcolumns.MSG_ID));
        }
        cursor.close();
        db.close();
        return id;
    }


    /**
     * 查询所有的消息
     * @return
     */
    public List<MsgContent> queryAllMessages() {
        List<MsgContent> mMsgContents= new ArrayList<MsgContent>();
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        String sql = "select * from " + DBcolumns.TABLE_MSG;
        Cursor mCursor = db.rawQuery(sql, null);
        while (mCursor.moveToNext()){
            String from = mCursor.getString(mCursor.getColumnIndex(DBcolumns.MSG_FROM));
            String to = mCursor.getString(mCursor.getColumnIndex(DBcolumns.MSG_TO));
            String type = mCursor.getString(mCursor.getColumnIndex(DBcolumns.MSG_TYPE));
            String content = mCursor.getString(mCursor.getColumnIndex(DBcolumns.MSG_CONTENT));
            String date = mCursor.getString(mCursor.getColumnIndex(DBcolumns.MSG_DATE));
            String isread = mCursor.getString(mCursor.getColumnIndex(DBcolumns.MSG_ISREADED));
            String fromClientid = mCursor.getString(mCursor.getColumnIndex(DBcolumns.MSG_FROM_CLIENTID));
            String toClientid = mCursor.getString(mCursor.getColumnIndex(DBcolumns.MSG_TO_CLIENTID));
            String msgName = mCursor.getString(mCursor.getColumnIndex(DBcolumns.MSG_NAME));
            MsgContent mMsgContent = new MsgContent(from,to,type,content,fromClientid,toClientid,date,isread,msgName);
            Flog.e(TAG,mMsgContent.toString());
            mMsgContents.add(mMsgContent);
        }
        db.close();
        return mMsgContents;
    }


    /**
     * 通过消息名查询所有的消息
     * @return
     */
    public List<MsgContent> queryMessagesByConstact(String constactName) {
        List<MsgContent> mMsgContents= new ArrayList<MsgContent>();
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        String sql = "select * from " + DBcolumns.TABLE_MSG+" where msg_name = ?";

        String[] tName = new String[] { constactName };
        Cursor mCursor = db.rawQuery(sql, tName);
        while (mCursor.moveToNext()){
            String from = mCursor.getString(mCursor.getColumnIndex(DBcolumns.MSG_FROM));
            String to = mCursor.getString(mCursor.getColumnIndex(DBcolumns.MSG_TO));
            String type = mCursor.getString(mCursor.getColumnIndex(DBcolumns.MSG_TYPE));
            String content = mCursor.getString(mCursor.getColumnIndex(DBcolumns.MSG_CONTENT));
            String date = mCursor.getString(mCursor.getColumnIndex(DBcolumns.MSG_DATE));
            String isread = mCursor.getString(mCursor.getColumnIndex(DBcolumns.MSG_ISREADED));
            String fromClientid = mCursor.getString(mCursor.getColumnIndex(DBcolumns.MSG_FROM_CLIENTID));
            String toClientid = mCursor.getString(mCursor.getColumnIndex(DBcolumns.MSG_TO_CLIENTID));
            String msgName = mCursor.getString(mCursor.getColumnIndex(DBcolumns.MSG_NAME));
            MsgContent mMsgContent = new MsgContent(from,to,type,content,fromClientid,toClientid,date,isread,msgName);
            Flog.e(TAG,mMsgContent.toString());
            mMsgContents.add(mMsgContent);
        }
        db.close();
        return mMsgContents;
    }




    /**
     * 插入新的一条msgcontent
     * @param msgContent
     * @return
     */
    public void insertNewMsgList(MsgContent msgContent) {
        Flog.e(TAG, "insertNewMsgList!!!");
        if (queryIsExistMsgName(msgContent)){
            updateMsgList(msgContent);
        }else {
            insertMsgList(msgContent);
        }

    }

    /**
     * 插入
     * @param msgContent
     */
    public void insertMsgList(MsgContent msgContent){
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBcolumns.LIST_FROM, msgContent.getFromName());
        values.put(DBcolumns.LIST_TO, msgContent.getToName());
        values.put(DBcolumns.LIST_NAME, msgContent.getMsgName());
        values.put(DBcolumns.LIST_TYPE, msgContent.getType());
        values.put(DBcolumns.LIST_CONTENT, msgContent.getContent());
        values.put(DBcolumns.LIST_DATE, msgContent.getDate());
        values.put(DBcolumns.LIST_ISREADED, msgContent.getIsRead());
        values.put(DBcolumns.LIST_FROM_CLIENTID, msgContent.getFromClientid());
        values.put(DBcolumns.LIST_TO_CLIENTID, msgContent.getToClientid());
        db.insert(DBcolumns.TABLE_MSGLIST, null, values);
        db.close();
    }

    /**
     * 更新msglist
     * @param msgContent
     */
    public void updateMsgList(MsgContent msgContent){
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        String sql = "UPDATE table_msg_list SET list_content = ?, list_date = ? ,list_type = ? WHERE list_name = ?";
        Object[] datas = new Object[]{new String(msgContent.getContent()), new String(msgContent.getDate()),new String(msgContent.getType()), new String(msgContent.getMsgName())};
        db.execSQL(sql, datas);
        db.close();
    }


    /**
     * 删除列表消息
     * @param msgContent
     * @return
     */
    public boolean deleteMsgListByName(MsgContent msgContent) {
        int isDelete;
        String[] tName = new String[] { msgContent.getMsgName() };
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        isDelete = db.delete(DBcolumns.TABLE_MSGLIST, DBcolumns.LIST_NAME + "=?", tName);
        deleteMsgByType(msgContent);
        return isDelete > 0;
    }


    /**
     * 删除消息
     * @param msgContent
     * @return
     */
    public void deleteMsgByType(MsgContent msgContent) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        String sql = "delete from table_msg where msg_name=? and msg_type=?";
        Object[] datas = new Object[]{new String(msgContent.getMsgName()), new String(msgContent.getType())};
        db.execSQL(sql, datas);
        db.close();
    }

    /**
     * 查询最新一条记录的id
     *
     * @return
     */
    public boolean queryIsExistMsgName(MsgContent msgContent) {
        boolean isExist = false;
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        String sql = "select * from " + DBcolumns.TABLE_MSGLIST +" where list_name = ?";
        String[] listName = new String[]{msgContent.getMsgName()};
        Cursor cursor = db.rawQuery(sql, listName);
        if (cursor.moveToNext()) {
            String type = cursor.getString(cursor.getColumnIndex(DBcolumns.LIST_TYPE));
            if (type.equals(msgContent.getType())){
                isExist = true;
            }else {
                isExist = false;
            }

        }
        cursor.close();
        db.close();
        Flog.e(TAG, "queryIsExistMsgName==" + isExist);
        return isExist;
    }


    /**
     * 通过消息名查询所有的消息
     * @return
     */
    public List<MsgContent> queryAllMsgList() {
        Flog.e(TAG,"queryAllMsgList!!!");
        List<MsgContent> mMsgContents= new ArrayList<MsgContent>();
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        String sql = "select * from " + DBcolumns.TABLE_MSGLIST;
        Cursor mCursor = db.rawQuery(sql, null);
        while (mCursor.moveToNext()){
            String from = mCursor.getString(mCursor.getColumnIndex(DBcolumns.LIST_FROM));
            String to = mCursor.getString(mCursor.getColumnIndex(DBcolumns.LIST_TO));
            String type = mCursor.getString(mCursor.getColumnIndex(DBcolumns.LIST_TYPE));
            String content = mCursor.getString(mCursor.getColumnIndex(DBcolumns.LIST_CONTENT));
            String date = mCursor.getString(mCursor.getColumnIndex(DBcolumns.LIST_DATE));
            String isread = mCursor.getString(mCursor.getColumnIndex(DBcolumns.LIST_ISREADED));
            String fromClientid = mCursor.getString(mCursor.getColumnIndex(DBcolumns.LIST_FROM_CLIENTID));
            String toClientid = mCursor.getString(mCursor.getColumnIndex(DBcolumns.LIST_TO_CLIENTID));
            String msgName = mCursor.getString(mCursor.getColumnIndex(DBcolumns.LIST_NAME));
            MsgContent mMsgContent = new MsgContent(from,to,type,content,fromClientid,toClientid,date,isread,msgName);
            mMsgContents.add(mMsgContent);
        }
        db.close();
        return mMsgContents;
    }

    public void createMsgList(){
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
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
        db.execSQL(sql_msg_list);
        db.close();
    }






}
