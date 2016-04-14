package com.suda.jzapp.manager.domain;

/**
 * Created by ghbha on 2016/4/13.
 */
public class MsgDo {

    private int msgType;

    private String msgContent;

    private String msgExtra;

    private String sendUser;

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMsgExtra() {
        return msgExtra;
    }

    public void setMsgExtra(String msgExtra) {
        this.msgExtra = msgExtra;
    }

    public String getSendUser() {
        return sendUser;
    }

    public void setSendUser(String sendUser) {
        this.sendUser = sendUser;
    }
}
