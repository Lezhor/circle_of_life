package com.android.circleoflife.database.control.daos;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import java.util.Collection;

public interface BaseDao<T> {

    @Insert()
    void insert(T entity);

    @Insert
    void insertAll(T... entities);

    @Insert
    void insertAll(Collection<T> entities);

    @Update()
    void update(T entity);

    @Delete
    void delete(T entity);

}
