package com.huang.utils;


import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.huang.entity.MsgContent;

/**
 * Created by huang on 12/31/15.
 */
public class HttpUtils {

    private static final String TAG = "HttpUtils";

    public static HttpUtils mHttpUtils;
    private Context mContext;

    public static HttpUtils getInstance() {
        if (mHttpUtils == null) {
            mHttpUtils = new HttpUtils();
        }
        return mHttpUtils;
    }

    public HttpUtils(Context context) {
        this.mContext = context;
    }

    public HttpUtils() {
        mWorker = new Worker(TAG);
        mHandler = new Handler(mWorker.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Constants.TYPE_ADD_FRIEND:
                        MsgContent msgContent = (MsgContent)msg.obj;
                        GeTuiUtils.pushMessage(msgContent.getToClientid(), JsonUtils.buildMsgContent(msgContent));
                        break;

                    case Constants.TYPE_TEXT:
                        MsgContent textContent = (MsgContent)msg.obj;
                        GeTuiUtils.pushMessage(textContent.getToClientid(), JsonUtils.buildMsgContent(textContent));
                        break;
                    case Constants.TYPE_QUERY_ONLINE:
                        String clientid = (String)msg.obj;
                        if(GeTuiUtils.getIsUserOnline(clientid)){
                            mHttpUtilsListener.isOnline(true);
                        }else{
                            mHttpUtilsListener.isOnline(false);
                        }
                        break;
                }
            }
        };

    }

    private Worker mWorker;
    private Handler mHandler = null;



    public void postAddFriend(MsgContent msgContent,int msgWhat){
        Message msg = new Message();
        msg.what = msgWhat;
        msg.obj = msgContent;
        mHandler.sendMessage(msg);
    }


    public void postTest(MsgContent msgContent){
        Message msg = new Message();
        msg.what = Constants.TYPE_TEXT;
        msg.obj = msgContent;
        mHandler.sendMessage(msg);
    }

    public void queryOnline(String clientid){
        Message msg = new Message();
        msg.what = Constants.TYPE_QUERY_ONLINE;
        msg.obj = clientid;
        mHandler.sendMessage(msg);
    }

    private HttpUtilsListener mHttpUtilsListener;
    public interface HttpUtilsListener {
        public void isOnline(boolean isOnline);
    }

    public void setHttpUtilsListener(HttpUtilsListener httpUtilsListener) {
        this.mHttpUtilsListener = httpUtilsListener;
    }




}
