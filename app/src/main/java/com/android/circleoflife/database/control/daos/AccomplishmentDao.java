package com.android.circleoflife.database.control.daos;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Ignore;
import androidx.room.Query;

import com.android.circleoflife.database.models.*;

import java.time.LocalDateTime;
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

    @Query("SELECT * FROM accomplishments WHERE userID = :userID AND timestamp >= :timestamp ORDER BY timestamp ASC")
    LiveData<List<Accomplishment>> getAllAccomplishmentsAfterTimestamp(UUID userID, LocalDateTime timestamp);

    @Ignore
    default LiveData<List<Accomplishment>> getAllAccomplishmentsAfterTimestamp(User user, @NonNull LocalDateTime timestamp) {
        return getAllAccomplishmentsAfterTimestamp(user.getId(), timestamp);
    }

    @Query("SELECT * FROM accomplishments WHERE userID = :userID AND timestamp < :timestamp ORDER BY timestamp ASC")
    LiveData<List<Accomplishment>> getAllAccomplishmentsBeforeTimestamp(UUID userID, LocalDateTime timestamp);

    @Ignore
    default LiveData<List<Accomplishment>> getAllAccomplishmentsBeforeTimestamp(User user, @NonNull LocalDateTime timestamp) {
        return getAllAccomplishmentsBeforeTimestamp(user.getId(), timestamp);
    }

    @Query("SELECT * FROM accomplishments WHERE userID = :userID AND timestamp >= :timestamp1 AND timestamp < :timestamp2 ORDER BY timestamp ASC")
    LiveData<List<Accomplishment>> getAllAccomplishmentsBetweenTimestamps(UUID userID, LocalDateTime timestamp1, LocalDateTime timestamp2);

    @Ignore
    default LiveData<List<Accomplishment>> getAllAccomplishmentsBetweenTimestamps(User user, LocalDateTime timestamp1, LocalDateTime timestamp2) {
        if (timestamp1 == null && timestamp2 == null) {
            return getAllAccomplishments(user.getId());
        } else if (timestamp1 == null) {
            return getAllAccomplishmentsBeforeTimestamp(user.getId(), timestamp2);
        } else if (timestamp2 == null) {
            return getAllAccomplishmentsAfterTimestamp(user.getId(), timestamp1);
        } else {
            return getAllAccomplishmentsBetweenTimestamps(user.getId(), timestamp1, timestamp2);
        }
    }
}
