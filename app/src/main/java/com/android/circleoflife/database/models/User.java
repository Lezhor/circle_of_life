package com.android.circleoflife.database.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.android.circleoflife.database.validators.StringValidator;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity(tableName = "users")
public class User {

    @PrimaryKey
    @ColumnInfo(name = "uid")
    private int id;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "creation_time")
    private LocalDateTime timeOfCreation;

    public User(String username, String password, LocalDateTime timeOfCreation) throws IllegalArgumentException {
        setUsername(username);
        setPassword(password);
        setTimeOfCreation(timeOfCreation);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
