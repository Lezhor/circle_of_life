package com.android.circleoflife.database.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

import com.android.circleoflife.logging.model.DBLog;

import java.time.LocalDateTime;

/**
 * This Model-Class is an Entry for room-database. It represents the table `logs`<br>
 * Its primary key is `uid, time`. It means you can't have two logs with the same time of creation.
 * However this shouldn't be possible anyway since they would need to be created in the same millisecond...
 */
@Entity(
        tableName = "logs",
        indices = {
                @Index(value = {"uid", "time"}, unique = true)
        },
        primaryKeys = {"uid", "time"},
        foreignKeys = {
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "uid",
                        childColumns = "uid",
                        onUpdate = ForeignKey.CASCADE,
                        onDelete = ForeignKey.RESTRICT
                )
        },
        inheritSuperIndices = true
)
public class LogEntity {
    @ColumnInfo(name = "uid")
    private int uid;

    @NonNull
    @ColumnInfo(name = "time")
    private LocalDateTime time;

    @NonNull
    @ColumnInfo(name = "content")
    private DBLog log;

    @Ignore
    public LogEntity(int uid, @NonNull DBLog log) {
        this(uid, log.getTimeOfCreation(), log);
    }

    LogEntity(int uid, @NonNull LocalDateTime time, @NonNull DBLog log) {
        this.uid = uid;
        this.time = time;
        this.log = log;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @NonNull
    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(@NonNull LocalDateTime time) {
        this.time = time;
    }

    @NonNull
    public DBLog getLog() {
        return log;
    }

    public void setLog(@NonNull DBLog log) {
        this.log = log;
    }
}
