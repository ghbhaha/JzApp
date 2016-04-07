package com.suda.jzapp.util;

import com.avos.avoscloud.AVException;

/**
 * Created by Suda on 2015/7/24.
 */
public class ExceptionInfoUtil {

    public static String getError(int errorCode) {
        String info = "";
        switch (errorCode) {
            case AVException.USERNAME_TAKEN:
                info = "用户名已被占用";
                break;
            case AVException.USER_DOESNOT_EXIST:
                info = "用户名未注册";
                break;
            case AVException.USERNAME_PASSWORD_MISMATCH:
                info = "密码错误";
                break;
            case AVException.INVALID_EMAIL_ADDRESS:
                info = "请填写正确邮箱";
                break;
            case AVException.INVALID_PHONE_NUMBER:
                info = "请填写正确手机号";
                break;
            case AVException.EMAIL_TAKEN:
                info = "该邮箱已被注册";
                break;
            case AVException.USER_MOBILE_PHONENUMBER_TAKEN:
                info = "该手机号已被注册";
                break;
            default:
                break;
        }
        return info;
    }
}
