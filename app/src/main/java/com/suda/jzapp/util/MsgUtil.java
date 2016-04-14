package com.suda.jzapp.util;

import com.alibaba.fastjson.JSON;
import com.suda.jzapp.manager.domain.MsgDo;
import com.suda.jzapp.misc.MsgConstant;

/**
 * Created by ghbha on 2016/4/13.
 */
public class MsgUtil {
    public static String getFormatMsg(int msgType, String content,String extra) {
        MsgDo msgDo = new MsgDo();
        msgDo.setMsgContent(content);
        msgDo.setMsgType(msgType);
        msgDo.setMsgExtra(extra);
        return JSON.toJSONString(msgDo);
    }

    public static MsgDo parseMsg(String orgMsg) {
        MsgDo msgDo = null;
        try {
            msgDo = JSON.parseObject(orgMsg, MsgDo.class);
        } catch (Exception e) {
            msgDo = new MsgDo();
            msgDo.setMsgType(MsgConstant.MSG_TYPE_INVALID);
            LogUtils.d(e.toString());
        }
        return msgDo;
    }
}
