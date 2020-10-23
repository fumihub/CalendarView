package com.non_name_hero.calenderview.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "schedule_group", indices = {@Index(value = {"group_id", "color_number"},
        unique = true)})
public class ScheduleGroup {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "group_id")
    @NonNull
    private int mGroupId;

    @NonNull
    @ColumnInfo(name = "color_number")
    private int mColorNumber;

    @NonNull
    @ColumnInfo(name = "group_name")
    private String mGroupName;

    @NonNull
    @ColumnInfo(name = "character_color")
    private String mCharacterColor;

    @NonNull
    @ColumnInfo(name = "background_color")
    private int mBackgroundColor;

    public ScheduleGroup(@NonNull int colorNumber,
                         @NonNull String groupName,
                         @NonNull String characterColor,
                         @NonNull int backgroundColor){
        mColorNumber = colorNumber;
        mGroupName = groupName;
        mCharacterColor = characterColor;
        mBackgroundColor = backgroundColor;
    }

    @Ignore
    public ScheduleGroup(@NonNull int groupId,
                         @NonNull int colorNumber,
                         @NonNull String groupName,
                         @NonNull String characterColor,
                         @NonNull int backgroundColor){
        mGroupId = groupId;
        mColorNumber = colorNumber;
        mGroupName = groupName;
        mCharacterColor = characterColor;
        mBackgroundColor = backgroundColor;
    }

    public void setGroupId(int groupId) {
        this.mGroupId = groupId;
    }
    public int getGroupId() {return mGroupId;}
    public int getColorNumber() {return mColorNumber;}
    public String getGroupName() {return mGroupName;}
    public String getCharacterColor() {return mCharacterColor;}
    public int getBackgroundColor() {return mBackgroundColor;}
}
