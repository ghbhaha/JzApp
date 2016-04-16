package com.suda.jzapp.dao.local.user;

import android.content.Context;

import com.suda.jzapp.dao.greendao.User;
import com.suda.jzapp.dao.greendao.UserDao;
import com.suda.jzapp.dao.local.BaseLocalDao;

/**
 * Created by ghbha on 2016/4/6.
 */
public class UserLocalDao extends BaseLocalDao {

    public User getUser(String userID, Context context) {
        UserDao userDao = getDaoSession(context).getUserDao();
        return getSingleData(userDao.queryBuilder().where(UserDao.Properties.UserId.eq(userID)).list());
    }

    public User getUserByUserName(String userName, Context context) {
        UserDao userDao = getDaoSession(context).getUserDao();
        return getSingleData(userDao.queryBuilder().where(UserDao.Properties.UserName.eq(userName)).list());
    }

    public void delUserByUserId(String userID, Context context) {
        UserDao userDao = getDaoSession(context).getUserDao();
        userDao.queryBuilder().where(UserDao.Properties.UserId.eq(userID)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public void clear(Context context) {
        UserDao userDao = getDaoSession(context).getUserDao();
        userDao.deleteAll();
    }

    public void insertUser(User user, Context context) {
        UserDao userDao = getDaoSession(context).getUserDao();
        userDao.insert(user);
    }

}
