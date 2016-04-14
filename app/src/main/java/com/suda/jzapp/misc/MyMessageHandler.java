package com.suda.jzapp.misc;

import android.content.Context;
import android.util.Log;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.suda.jzapp.manager.domain.MsgDo;
import com.suda.jzapp.util.MsgUtil;

/**
 * Created by ghbha on 2016/4/13.
 */
public class MyMessageHandler extends AVIMMessageHandler {


    private Context mContext;

    public MyMessageHandler(Context context) {
        this.mContext = context;
    }

    @Override
    public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
        Log.d(TAG, ((AVIMTextMessage) message).getText().toString());
        MsgDo msgDo = MsgUtil.parseMsg(((AVIMTextMessage) message).getText().toString());
        if (msgDo.getMsgType() == MsgConstant.MSG_TYPE_LINK_REQUEST
                || msgDo.getMsgType() == MsgConstant.MSG_TYPE_LINK_ACCEPT
                || msgDo.getMsgType() == MsgConstant.MSG_TYPE_LINK_CANCEL
                ) {
            msgDo.setSendUser(message.getFrom());
            if (msgCallBack != null) {
                msgCallBack.show(msgDo);
            }
        }
    }

    @Override
    public void onMessageReceipt(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {

    }

    public void setMsgCallBack(MsgCallBack msgCallBack) {
        this.msgCallBack = msgCallBack;
    }

    private MsgCallBack msgCallBack;

    private final static String TAG = "Message";


    public interface MsgCallBack {
        void show(MsgDo msgDo);
    }
}
