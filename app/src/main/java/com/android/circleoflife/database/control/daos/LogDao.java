package com.android.circleoflife.database.control.daos;

import androidx.room.Dao;

import com.android.circleoflife.database.models.LogEntity;

@Dao
public interface LogDao extends BaseDao<LogEntity> {

    // TODO: 10.12.2023 methods: getAll(), getAllAfter() etc.

}
