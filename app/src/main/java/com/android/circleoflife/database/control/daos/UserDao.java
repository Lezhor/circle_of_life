package com.android.circleoflife.database.control.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.android.circleoflife.database.models.User;

import java.util.UUID;

@Dao
public interface UserDao extends BaseDao<User> {

    @Query("SELECT * FROM users WHERE userID = :userID")
    LiveData<User> getUser(UUID userID);

    @Query("SELECT * FROM users WHERE username LIKE :username LIMIT 1")
    LiveData<User> getUser(String username);

}
