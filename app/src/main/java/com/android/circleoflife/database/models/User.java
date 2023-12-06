package com.android.circleoflife.database.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

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

    // TODO: 06.12.2023 Create Constructor for User Model

    @Ignore
    public User() {

    }

    public User(String username, String password, LocalDateTime timeOfCreation) throws IllegalArgumentException {
        this.username = username;
        this.password = password;
        this.timeOfCreation = timeOfCreation;
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
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getTimeOfCreation() {
        return timeOfCreation;
    }

    public void setTimeOfCreation(LocalDateTime timeOfCreation) {
        this.timeOfCreation = timeOfCreation;
    }
}
