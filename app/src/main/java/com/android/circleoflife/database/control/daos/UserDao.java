package com.android.circleoflife.database.control.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.android.circleoflife.database.control.daos.BaseDao;
import com.android.circleoflife.database.models.User;

@Dao
public interface UserDao extends BaseDao<User> {

    @Query("SELECT * FROM users WHERE uid = :uid")
    LiveData<User> getUser(int uid);

    @Query("SELECT * FROM users WHERE username LIKE :username LIMIT 1")
    LiveData<User> getUser(String username);

}
