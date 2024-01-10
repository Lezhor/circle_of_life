package com.android.circleoflife.database.control.daos;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Ignore;
import androidx.room.Query;

import com.android.circleoflife.database.models.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Dao
public interface AccomplishmentDao extends BaseDao<Accomplishment> {

    @Query("SELECT * FROM accomplishments WHERE userID = :userID ORDER BY timestamp ASC")
    LiveData<List<Accomplishment>> getAllAccomplishments(UUID userID);

    @Ignore
    default LiveData<List<Accomplishment>> getAllAccomplishments(User user) {
        return getAllAccomplishments(user.getId());
    }

    @Query("SELECT * FROM accomplishments WHERE cycleID IS NOT NULL AND cycleID IN (SELECT ID FROM cycles WHERE userID = :userID AND categoryID = :categoryID) OR todoID IS NOT NULL AND todoID IN (SELECT ID FROM todos WHERE userID = :userID AND categoryID = :categoryID) ORDER BY timestamp ASC")
    LiveData<List<Accomplishment>> getAllAccomplishments(UUID userID, UUID categoryID);

    @Ignore
    default LiveData<List<Accomplishment>> getAllAccomplishments(Category category) {
        return getAllAccomplishments(category.getUserID(), category.getId());
    }

    @Query("SELECT * FROM accomplishments WHERE userID = :userID AND date > :date OR date = :date AND timestamp >= :timestamp ORDER BY timestamp ASC")
    LiveData<List<Accomplishment>> getAllAccomplishmentsAfterTimestamp(UUID userID, LocalDate date, LocalTime timestamp);

    @Ignore
    default LiveData<List<Accomplishment>> getAllAccomplishmentsAfterTimestamp(User user, @NonNull LocalDateTime timestamp) {
        return getAllAccomplishmentsAfterTimestamp(user.getId(), timestamp.toLocalDate(), timestamp.toLocalTime());
    }

    @Query("SELECT * FROM accomplishments WHERE userID = :userID AND date < :date OR date = :date AND (timestamp IS NULL OR timestamp < :timestamp) ORDER BY timestamp ASC")
    LiveData<List<Accomplishment>> getAllAccomplishmentsBeforeTimestamp(UUID userID, LocalDate date, LocalTime timestamp);

    @Ignore
    default LiveData<List<Accomplishment>> getAllAccomplishmentsBeforeTimestamp(User user, @NonNull LocalDateTime timestamp) {
        return getAllAccomplishmentsBeforeTimestamp(user.getId(), timestamp.toLocalDate(), timestamp.toLocalTime());
    }

    @Query("SELECT * FROM accomplishments WHERE userID = :userID AND (date > :date1 OR date = :date1 AND (timestamp IS NULL OR timestamp >= :timestamp1)) AND (date < :date2 OR date = :date2 AND timestamp < :timestamp2) ORDER BY timestamp ASC")
    LiveData<List<Accomplishment>> getAllAccomplishmentsBetweenTimestamps(UUID userID, LocalDate date1, LocalTime timestamp1, LocalDate date2, LocalTime timestamp2);

    @Ignore
    default LiveData<List<Accomplishment>> getAllAccomplishmentsBetweenTimestamps(User user, LocalDateTime timestamp1, LocalDateTime timestamp2) {
        if (timestamp1 == null && timestamp2 == null) {
            return getAllAccomplishments(user.getId());
        } else if (timestamp1 == null) {
            return getAllAccomplishmentsBeforeTimestamp(user.getId(), timestamp2.toLocalDate(), timestamp2.toLocalTime());
        } else if (timestamp2 == null) {
            return getAllAccomplishmentsAfterTimestamp(user.getId(), timestamp1.toLocalDate(), timestamp1.toLocalTime());
        } else {
            return getAllAccomplishmentsBetweenTimestamps(user.getId(), timestamp1.toLocalDate(), timestamp1.toLocalTime(), timestamp2.toLocalDate(), timestamp2.toLocalTime());
        }
    }

    /**
     * Deletes EVERYTHING from accomplishments table - use with caution
     */
    @Query("DELETE FROM accomplishments")
    void deleteEverythingFromTable();

}
