package com.suda.jzapp.dao.cloud.avos.pojo.user;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

import java.util.List;

/**
 * Created by ghbha on 2016/4/14.
 */
@AVClassName("UserLink")
public class UserLink extends AVObject {

    public List<String> getMembers() {
        return getList(MEMBER);
    }

    public void setMember(List<String> list) {
        put(MEMBER, list);
    }

    public final static String MEMBER = "member";
}
