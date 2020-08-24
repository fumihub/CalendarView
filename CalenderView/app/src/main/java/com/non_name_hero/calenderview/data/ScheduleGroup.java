package com.non_name_hero.calenderview.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "schedule_group")
public class ScheduleGroup {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "color_number")
    private int mColorNumber;

    @NonNull
    @ColumnInfo(name = "group_name")
    private String mGroupName;

    @NonNull
    @ColumnInfo(name = "character_color")
    private int mCharacterNumber;

    @NonNull
    @ColumnInfo(name = "background_color")
    private int mBackgroundColor;

    public ScheduleGroup(@NonNull int colorNumber,
                         @NonNull String groupName,
                         @NonNull int character_color,
                         @NonNull int backgroundColor){
        mColorNumber = colorNumber;
        mGroupName = groupName;
        mCharacterNumber = character_color;
        mBackgroundColor = backgroundColor;
    }

}