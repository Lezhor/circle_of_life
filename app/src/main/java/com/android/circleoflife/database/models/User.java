package com.android.circleoflife.database.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(tableName = "users")
public class User {

    @PrimaryKey
    @ColumnInfo(name = "uid")
    private int id;

    @ColumnInfo(name = "username")
    private String userName;

    @ColumnInfo(name = "creation_time")
    private LocalDateTime dateTimeCreation;

    // TODO: 06.12.2023 Create Constructor for User Model

}
