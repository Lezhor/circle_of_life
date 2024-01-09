package com.android.circleoflife.database.control.daos;

import androidx.room.Dao;
import androidx.room.Ignore;
import androidx.room.Query;

import com.android.circleoflife.database.models.LogEntity;
import com.android.circleoflife.database.models.User;
import com.android.circleoflife.logging.model.DBLog;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Dao
public interface LogDao extends BaseDao<LogEntity> {

    // TODO: 10.12.2023 methods: getAll(), getAllAfter() etc.

    /**
     * Retrieves all from database with passed userID, and where the timestamp is between timestamp1 and timestamp2
     * @param userID userID
     * @param timestamp1 min timestamp (inclusive)
     * @param timestamp2 max timestamp (exclusive)
     * @return a list of all logs matching the criteria, sorted by timestamp
     */
    @Query("SELECT * FROM logs WHERE userID = :userID AND timestamp >= :timestamp1 AND timestamp < :timestamp2 ORDER BY timestamp")
    List<LogEntity> getLogsBetweenTimestamps(UUID userID, LocalDateTime timestamp1, LocalDateTime timestamp2);

    /**
     * calls {@link #getLogsBetweenTimestamps(UUID, LocalDateTime, LocalDateTime)}, converts result to an array of type DBLog
     * @param user user
     * @param timestamp1 min timestamp (inclusive)
     * @param timestamp2 max timestamp (exculusive)
     * @return an array of all logs matching the criteria, sorted by timestamp
     */
    @Ignore
    default DBLog<?>[] getLogsBetweenTimestamps(User user, LocalDateTime timestamp1, LocalDateTime timestamp2) {
        return getLogsBetweenTimestamps(user.getId(), timestamp1, timestamp2)
                .stream()
                .map(LogEntity::getLog)
                .toArray(DBLog[]::new);
    }

}
