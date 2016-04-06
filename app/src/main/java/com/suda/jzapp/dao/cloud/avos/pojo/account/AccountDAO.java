package com.suda.jzapp.dao.cloud.account;

import com.suda.jzapp.manager.CallBack;

/**
 * Created by Suda on 2015/12/7.
 */
public interface AccountDAO {

    /**
     * 根据用户名获取全部账户信息
     *
     * @param userName
     * @param callBack
     */
    void getAllAccount(String userName, CallBack callBack);


}
