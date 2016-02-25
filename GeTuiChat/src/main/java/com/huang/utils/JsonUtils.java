package com.huang.utils;

import com.huang.entity.MsgContent;
import com.huang.entity.User;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by huang on 12/26/15.
 */
public class JsonUtils {

    public static String TAG = "JsonUtils";

    public static String userJsonObject(User user) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", user.getUsername());
            jsonObject.put("password", user.getPassword());
            jsonObject.put("clientid", user.getClientid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Flog.e(TAG, jsonObject.toString());
        return jsonObject.toString();
    }

    /**
     * 构造消息体
     *
     * @param msgContent
     * @return
     */
    public static String buildMsgContent(MsgContent msgContent) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("from", msgContent.getFromName());
            jsonObject.put("to", msgContent.getToName());
            jsonObject.put("type", msgContent.getType());
            jsonObject.put("isread", msgContent.getIsRead());
            jsonObject.put("content", msgContent.getContent());
            jsonObject.put("date", msgContent.getDate());
            jsonObject.put("fromclientid", msgContent.getFromClientid());
            jsonObject.put("toclientid", msgContent.getToClientid());
            jsonObject.put("msgname", msgContent.getMsgName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Flog.e(TAG, jsonObject.toString());
        return jsonObject.toString();
    }

    /**
     * 获取当前的时间
     *
     * @return
     */
    public static String getCurrentDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date mDate = new Date(System.currentTimeMillis());
        return df.format(mDate);
    }


    /**
     * 获取msgcontent
     * @param data
     * @return
     */
    public static MsgContent parseMsgContent(String data) {
        MsgContent msgContent = null;
        try {
            JSONObject mJsonObject = new JSONObject(data);
            msgContent = new MsgContent();
            String date =mJsonObject.getString("date");
            String from = mJsonObject.getString("from");
            String fromClientid = mJsonObject.getString("fromclientid");
            String to = mJsonObject.getString("to");
            String toClientid = mJsonObject.getString("toclientid");
            String type = mJsonObject.getString("type");
            String isread = mJsonObject.getString("isread");
            String content = mJsonObject.getString("content");
            String msgName = from;
            msgContent = new MsgContent(from,to,type,content,fromClientid,toClientid,date,isread,msgName);
            Flog.e(TAG,"parseMsgContent==="+msgContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msgContent;
    }

}
