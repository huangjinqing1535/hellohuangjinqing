package com.huang.utils;

import com.gexin.rp.sdk.base.IAliasResult;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.IQueryResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.huang.entity.User;

import java.util.List;
import java.util.Map;

/**
 * Created by huang on 12/28/15.
 */
public class GeTuiUtils {

    private static final String TAG = "GeTuiUtils";

    public static void main(String[] args) throws Exception {
        queryAliasIsBind(new User("huang", "jin", "dda225c8bd29bf8323aa70a2f153a806"));
    }

    /**
     * 发送消息到好友
     *
     * @param clientid
     * @param content
     */

    public static void pushMessage(String clientid, String content) {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(Constants.APPID);
        template.setAppkey(Constants.APPKEY);
        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        template.setTransmissionType(2);
        template.setTransmissionContent(content);
        IGtPush push = new IGtPush(Constants.GETUIHOST, Constants.APPKEY, Constants.MASTER);

        IQueryResult abc = push.getClientIdStatus(Constants.APPID, clientid);
        System.out.println(" @@@@@@@@@@@@ clientid : " + clientid + " , " + abc.getResponse());

        SingleMessage message = new SingleMessage();
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 3600 * 1000);
        message.setData(template);
        message.setPushNetWorkType(0);
        // 可选。判断是否客户端是否wifi环境下推送，1为在WIFI环境下，0为不限制网络环境。
        Target target = new Target();
        target.setAppId(Constants.APPID);
        target.setClientId(clientid);
        IPushResult ret = null;
        try {
            Flog.e(TAG, "个推发送数据.....");
            ret = push.pushMessageToSingle(message, target);
        } catch (RequestException e) {
            e.printStackTrace();
            ret = push.pushMessageToSingle(message, target, e.getRequestId());
        }
        if (ret != null) {
            Flog.e(TAG,ret.getResponse().toString());
        } else {
            Flog.e(TAG, "服务器响应异常");
        }
    }

    /**
     * 绑定别名
     *
     * @param alias
     * @param clientid
     * @return
     * @throws Exception
     */
    public static boolean bindAlias(String alias, String clientid) {
        IGtPush push = new IGtPush(Constants.GETUIHOST, Constants.APPKEY, Constants.MASTER);
        // 单个CID绑定别名
        IAliasResult bindSCid = push.bindAlias(Constants.APPID, alias, clientid);
        Flog.e(TAG, "bindAlias绑定结果：" + bindSCid.getResult() + "错误码:"
                + bindSCid.getErrorMsg());
        return bindSCid.getResult();
    }

    /**
     * 通过别名获取clientid
     *
     * @return
     */
    public static String queryClientId(User user) {
        String clientid = null;
        IGtPush push = new IGtPush(Constants.GETUIHOST, Constants.APPKEY, Constants.MASTER);
        IAliasResult queryClient = push.queryClientId(Constants.APPID, user.getUsername());
        List<String> clientids = queryClient.getClientIdList();
        if (clientids.size() == 1) {
            clientid = clientids.get(0);
        }
        Flog.e(TAG, "根据别名获取的CID：" + clientid);
        return clientid;
    }

    /**
     * 通过别名获取clientid
     *
     * @param user
     * @return
     */
    public static boolean queryAliasIsBind(User user) {
        boolean isBind = false;
        IGtPush push = new IGtPush(Constants.GETUIHOST, Constants.APPKEY, Constants.MASTER);
        IAliasResult queryClient = push.queryClientId(Constants.APPID, user.getUsername());
        List<String> clientids = queryClient.getClientIdList();
        System.out.print("clientids==" + clientids);
        if (clientids == null) {
            if (bindAlias(user.getUsername(), user.getClientid())) {
                isBind = true;
            }
        }
        return isBind;
    }

    public static boolean queryAilas(User user) {
        boolean isAilas = false;
        IGtPush push = new IGtPush(Constants.GETUIHOST, Constants.APPKEY, Constants.MASTER);
        IAliasResult queryRet = push.queryAlias(Constants.APPID, user.getClientid());
        System.out.println("根据cid获取别名：" + queryRet.getAlias());
        if (queryRet.getAlias() == null || queryRet.getAlias().equals("")) {
            isAilas = true;
        }
        return isAilas;
    }


    public static String queryAilasByClientid(String clientid) {
        String username = null;
        IGtPush push = new IGtPush(Constants.GETUIHOST, Constants.APPKEY, Constants.MASTER);
        IAliasResult queryRet = push.queryAlias(Constants.APPID,clientid);
        System.out.println("根据cid获取别名：" + queryRet.getAlias());
        if (queryRet.getAlias() != null) {
            username = queryRet.getAlias();
        }
        return username;
    }


    /**
     * 搜索用户名
     *
     * @return
     */
    public static String searchUser(String username) {
        String clientid = null;
        IGtPush push = new IGtPush(Constants.GETUIHOST, Constants.APPKEY, Constants.MASTER);
        IAliasResult queryClient = push.queryClientId(Constants.APPID, username);
        List<String> clientids = queryClient.getClientIdList();
        if (clientids!=null){
            if (clientids.size() == 1) {
                clientid = clientids.get(0);
            }
        }
        Flog.e(TAG, "searchUser=====clientid===：" + clientid);
        return clientid;
    }


    /**
     * 获取车机状态
     *
     * @param clientID
     * @return
     */
    public static boolean getIsUserOnline(String clientID) {
        boolean isOnline = true;
        IGtPush push = new IGtPush(Constants.GETUIHOST, Constants.APPKEY, Constants.MASTER);
        IQueryResult queryResult = push.getClientIdStatus(Constants.APPID, clientID);
        Flog.e(TAG, " getIsUserOnline clientid : "
                + clientID + " , " + queryResult.getResponse());
        Map<String, Object> statuMap = queryResult.getResponse();
        String statusString = (String) statuMap.get("result");
        if (statusString.equals("Online")) {
            isOnline = true;
        } else {
            isOnline = false;
        }
        return isOnline;
    }


}
