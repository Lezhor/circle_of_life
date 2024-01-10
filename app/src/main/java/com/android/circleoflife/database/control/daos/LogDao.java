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
     * @param timestamp2 max timestamp (exclusive)
     * @return an array of all logs matching the criteria, sorted by timestamp
     */
    @Ignore
    default DBLog<?>[] getLogsBetweenTimestamps(User user, LocalDateTime timestamp1, LocalDateTime timestamp2) {
        if (timestamp1 == null && timestamp2 == null) {
            return getLogs(user);
        } else if (timestamp1 == null) {
            return getLogsBeforeTimestamp(user, timestamp2);
        } else if (timestamp2 == null) {
            return getLogsAfterTimestamp(user, timestamp1);
        } else {
            return getLogsBetweenTimestamps(user.getId(), timestamp1, timestamp2)
                    .stream()
                    .map(LogEntity::getLog)
                    .toArray(DBLog[]::new);
        }
    }

    @Query("SELECT * FROM logs WHERE userID = :userID AND timestamp < :timestamp ORDER BY timestamp")
    List<LogEntity> getLogsBeforeTimestamp(UUID userID, LocalDateTime timestamp);

    /**
     * Gets all logs of a user before the given timestamp
     * @param user user
     * @param timestamp max timestamp (exclusive)
     * @return array of all logs before certain timestamp
     */
    @Ignore
    default DBLog<?>[] getLogsBeforeTimestamp(User user, LocalDateTime timestamp) {
        if (timestamp == null) {
            return getLogs(user);
        } else {
            return getLogsBeforeTimestamp(user.getId(), timestamp)
                    .stream()
                    .map(LogEntity::getLog)
                    .toArray(DBLog[]::new);
        }
    }

    @Query("SELECT * FROM logs WHERE userID = :userID AND timestamp >= :timestamp ORDER BY timestamp")
    List<LogEntity> getLogsAfterTimestamp(UUID userID, LocalDateTime timestamp);

    /**
     * Gets all logs of a user after the given timestamp
     * @param user user
     * @param timestamp min timestamp (inclusive)
     * @return array of all logs before certain timestamp
     */
    @Ignore
    default DBLog<?>[] getLogsAfterTimestamp(User user, LocalDateTime timestamp) {
        if (timestamp == null) {
            return getLogs(user);
        } else {
            return getLogsAfterTimestamp(user.getId(), timestamp)
                    .stream()
                    .map(LogEntity::getLog)
                    .toArray(DBLog[]::new);
        }
    }

    @Query("SELECT * FROM logs WHERE userID = :userID ORDER BY timestamp")
    List<LogEntity> getLogs(UUID userID);

    /**
     * Returns an array of all logs of a user
     * @param user user
     * @return array of all logs
     */
    @Ignore
    default DBLog<?>[] getLogs(User user) {
        return getLogs(user.getId())
                .stream()
                .map(LogEntity::getLog)
                .toArray(DBLog[]::new);
    }

}
