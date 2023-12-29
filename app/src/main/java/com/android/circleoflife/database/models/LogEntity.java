package com.android.circleoflife.database.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.android.circleoflife.logging.model.DBLog;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * This Model-Class is an Entry for room-database. It represents the table `logs`<br>
 * Its primary key is `userID, timestamp`. It means you can't have two logs with the same timestamp of creation.
 * However this shouldn't be possible anyway since they would need to be created in the same millisecond...
 */
@Entity(
        tableName = "logs",
        indices = {
                @Index(value = {"ID", "userID"})
        },
        foreignKeys = {
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "userID",
                        childColumns = "userID",
                        onUpdate = ForeignKey.CASCADE,
                        onDelete = ForeignKey.RESTRICT
                )
        },
        inheritSuperIndices = true
)
public class LogEntity {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "ID")
    private UUID id;

    @NonNull
    @ColumnInfo(name = "userID")
    private UUID userID;

    @NonNull
    @ColumnInfo(name = "timestamp")
    private LocalDateTime timestamp;

    @NonNull
    @ColumnInfo(name = "content")
    private DBLog<?> log;

    @Ignore
    public LogEntity(@NonNull DBLog<?> log) {
        this(log.getId(), log.getUserId(), LocalDateTime.now(), log);
    }

    LogEntity(@NonNull UUID id, @NonNull UUID userID, @NonNull LocalDateTime timestamp, @NonNull DBLog<?> log) {
        this.id = id;
        this.userID = userID;
        this.timestamp = timestamp;
        this.log = log;
    }

    @NonNull
    public UUID getUserID() {
        return userID;
    }

    public void setUserID(@NonNull UUID userID) {
        this.userID = userID;
    }

    @NonNull
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NonNull LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @NonNull
    public DBLog<?> getLog() {
        return log;
    }

    public void setLog(@NonNull DBLog<?> log) {
        this.log = log;
    }

    @NonNull
    public UUID getId() {
        return id;
    }

    public void setId(@NonNull UUID id) {
        this.id = id;
    }
}
