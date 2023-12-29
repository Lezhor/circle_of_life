package com.android.circleoflife.database.control.daos;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.android.circleoflife.database.models.additional.HasUserId;
import com.android.circleoflife.logging.model.DBLog;

public interface BaseDao<T extends HasUserId> {

    @Insert
    void insert(T... entities);

    @Update()
    void update(T entity);

    @Delete
    void delete(T entity);

    /**
     * Executes the content of a {@link DBLog}
     * @param log log to be executed
     */
    default void executeLog(DBLog<T> log) {
        switch (log.getChangeMode()) {
            case INSERT -> insert(log.getChangedObject());
            case UPDATE -> update(log.getChangedObject());
            case DELETE -> delete(log.getChangedObject());
        }
    }

}
