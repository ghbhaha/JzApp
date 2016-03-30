package com.suda.jzapp.dao.cloud.account;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.suda.jzapp.dao.cloud.avos.pojo.AVAccount;
import com.suda.jzapp.dao.greendao.Account;
import com.suda.jzapp.manager.CallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suda on 2015/12/7.
 */
public class AccountDAOAVOSImpl implements AccountDAO {

    @Override
    public void getAllAccount(String userName, final CallBack callBack) {
        AVQuery<AVAccount> query = new AVQuery<AVAccount>();
        query.whereEqualTo(AVAccount.USER_ID, userName);
        query.findInBackground(new FindCallback<AVAccount>() {
            @Override
            public void done(List<AVAccount> list, AVException e) {
                if (e != null) {
                    callBack.doError();
                } else {
                    List<Account> result = new ArrayList<Account>();
                    for (AVAccount avAccount : list) {
                        Account account = new Account();
                        //
                        //
                        //
                        result.add(account);
                    }
                    callBack.doMyBusiness(result);
                }
            }
        });
    }


}
