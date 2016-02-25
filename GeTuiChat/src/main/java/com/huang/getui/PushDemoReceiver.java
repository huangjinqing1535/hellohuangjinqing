package com.huang.getui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.huang.server.MessageService;
import com.huang.utils.Flog;
import com.huang.utils.PreferencesUtils;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;

public class PushDemoReceiver extends BroadcastReceiver {

    public static final String TAG = "PushDemoReceiver";



    /**
     * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiSdkDemoActivity.tLogView == null)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Flog.d(TAG, "onReceive() action=" + bundle.getInt("action"));


        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                // String appid = bundle.getString("appid");
                byte[] payload = bundle.getByteArray("payload");

                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");

                // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
                boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
                Flog.d(TAG, "第三方回执接口调用" + (result ? "成功" : "失败"));

                if (payload != null) {
                    String data = new String(payload);
                    Flog.d(TAG, "receiver GET_MSG_DATA : " + data);
                    MessageService.getInstance(context).handleMessage(data);
                }
                break;
            case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID)
                // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
                String cid = bundle.getString("clientid");
                Flog.d(TAG, "cid======" + cid);
                if(cid!=null&&!cid.equals("")){
                    PreferencesUtils.putSharePreString(context,"clientid",cid);
                    PreferencesUtils.putSharePreString(context,"state","online");
                }
                break;

            case PushConsts.THIRDPART_FEEDBACK:
                break;
        }
    }



}
