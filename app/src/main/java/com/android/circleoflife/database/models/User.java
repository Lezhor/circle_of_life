package com.android.circleoflife.database.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.android.circleoflife.database.models.additional.Copyable;
import com.android.circleoflife.database.models.additional.HasUserId;
import com.android.circleoflife.database.validators.StringValidator;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity(tableName = "users", indices = @Index(value = {"userID"}))
public class User implements HasUserId, Copyable<User> {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "userID")
    private UUID id;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "creation_time")
    private LocalDateTime timeOfCreation;

    public User(@NonNull UUID id, String username, String password, LocalDateTime timeOfCreation) throws IllegalArgumentException {
        this.id = id;
        setUsername(username);
        setPassword(password);
        setTimeOfCreation(timeOfCreation);
    }

    @NonNull
    public UUID getId() {
        return id;
    }

    public void setId(@NonNull UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = StringValidator.validateUsername(username);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = StringValidator.validatePassword(password);
    }

    public LocalDateTime getTimeOfCreation() {
        return timeOfCreation;
    }

    public void setTimeOfCreation(LocalDateTime timeOfCreation) {
        this.timeOfCreation = Objects.requireNonNull(timeOfCreation);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof User that) {
            return this.id.equals(that.id);
        }
        return false;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" + username + "}";
    }

    @NonNull
    @Override
    public User copy() {
        return new User(id, username, password, timeOfCreation);
    }

    @NonNull
    @Override
    public UUID getUserID() {
        return getId();
    }
}
