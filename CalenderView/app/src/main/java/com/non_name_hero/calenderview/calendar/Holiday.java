package com.non_name_hero.calenderview.calendar;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "holidays")
public class Holiday {
    @PrimaryKey
    public Date date;

    @ColumnInfo
    public String name;
}
