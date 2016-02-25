package com.huang.entity;

/**
 * Created by huang on 1/5/16.
 * 消息体
 */
public class MsgContent {

    //从哪来的
    private String fromName;
    //到哪去的
    private String toName;
    //信息的类型:01为请求为好友，02为文本聊天信息，03为图片聊天信息，
    private String type;
    //信息的内容
    private String content;
    //发起者的clientid
    private String fromClientid;
    //到达者的clientid
    private String toClientid;
    //消息的时间
    private String date;
    //消息是否已读
    private String isRead;

    //
    private String msgName;

    public MsgContent() {
    }

    public String getMsgName() {
        return msgName;
    }

    public void setMsgName(String msgName) {
        this.msgName = msgName;
    }


    public MsgContent(String fromName, String toName, String type, String content, String fromClientid, String toClientid, String date, String isRead, String msgName) {
        this.fromName = fromName;
        this.toName = toName;
        this.type = type;
        this.content = content;
        this.fromClientid = fromClientid;
        this.toClientid = toClientid;
        this.date = date;
        this.isRead = isRead;
        this.msgName = msgName;
    }

    @Override
    public String toString() {
        return "MsgContent{" +
                "fromName='" + fromName + '\'' +
                ", toName='" + toName + '\'' +
                ", type='" + type + '\'' +
                ", content='" + content + '\'' +
                ", fromClientid='" + fromClientid + '\'' +
                ", toClientid='" + toClientid + '\'' +
                ", date='" + date + '\'' +
                ", isRead='" + isRead + '\'' +
                ", msgName='" + msgName + '\'' +
                '}';
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFromClientid() {
        return fromClientid;
    }

    public void setFromClientid(String fromClientid) {
        this.fromClientid = fromClientid;
    }

    public String getToClientid() {
        return toClientid;
    }

    public void setToClientid(String toClientid) {
        this.toClientid = toClientid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }
}
