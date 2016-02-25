package com.huang.server;

import android.content.Context;
import android.widget.Toast;

import com.huang.database.util.DBDao;
import com.huang.entity.Constact;
import com.huang.entity.MsgContent;
import com.huang.utils.Constants;
import com.huang.utils.Flog;
import com.huang.utils.JsonUtils;

/**
 * Created by huang on 12/30/15.
 * 消息处理
 */
public class MessageService {

    private static MessageService mMessageService;
    private Context mContext;
    private static final String TAG = "MessageService";

    public static MessageService getInstance(Context context){
        if (mMessageService==null){
            mMessageService = new MessageService(context);
        }
        return mMessageService;
    }

    public MessageService(Context context) {
        this.mContext = context;
    }

    public void handleMessage(String data){
        Flog.e(TAG, "data=====" + data);
        MsgContent msgContent = JsonUtils.parseMsgContent(data);
        if (msgContent!=null){
            DBDao.getInstance(mContext).insertMessage(msgContent);
            toSwitch(msgContent);
            if (mMessageListener!=null){
                mMessageListener.getMsgData(msgContent);
            }

        }
    }



    /**
     * 处理消息
     *
     * @param msgContent
     */
    public void toSwitch(MsgContent msgContent) {
        String type = msgContent.getType();
        if (type == null) {
            return;
        }
        switch (Integer.parseInt(type)) {
            case Constants.TYPE_ADD_FRIEND:
                handleAddFriend(msgContent);
                break;
        }
    }

    private void handleAddFriend(MsgContent msgContent) {
        String content = msgContent.getContent();
        if (content.equals("00") || content.equals("01") || content.equals("02")) {
            switch (Integer.parseInt(content)) {
                case 00:
                    break;
                case 01:
                    Constact mConstact = new Constact();
                    mConstact.setState("online");
                    mConstact.setName(msgContent.getFromName());
                    mConstact.setClientid(msgContent.getFromClientid());
                    mConstact.setGroupName("好友");
                    DBDao.getInstance(mContext).insertConstact(mConstact);
                    DBDao.getInstance(mContext).deleteMsgListByName(msgContent);
                    break;
                case 02:

                    break;
            }
        }

    }


    private MessageListener mMessageListener;
    public interface MessageListener {
        public void getMsgData(MsgContent msgContent);
    }

    public void setMessageListener(MessageListener messageListener) {
        this.mMessageListener = messageListener;
    }


}
