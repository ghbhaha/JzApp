package com.suda.jzapp.misc;

/**
 * Created by ghbha on 2016/4/13.
 */
public class MsgConstant {

    //非法消息类型
    public final static int MSG_TYPE_INVALID = -1;
    //普通消息
    public final static int MSG_TYPE_COMMON = 0;
    //请求关联
    public final static int MSG_TYPE_LINK_REQUEST = 1;
    //接受关联
    public final static int MSG_TYPE_LINK_ACCEPT = 2;
    //取消关联
    public final static int MSG_TYPE_LINK_CANCEL = 3;
    //解除关联
    public final static int MSG_TYPE_LINK_BROKE = 4;
}
