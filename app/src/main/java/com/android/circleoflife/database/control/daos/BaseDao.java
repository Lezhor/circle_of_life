package com.android.circleoflife.database.control.daos;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

public interface BaseDao<T> {

    @Insert
    void insert(T... entities);

    @Update()
    void update(T entity);

    @Delete
    void delete(T entity);

}
