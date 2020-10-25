package com.non_name_hero.calenderview.data

import androidx.room.*

@Entity(tableName = "schedule_group", indices = [Index(value = ["group_id", "color_number"], unique = true)])
class ScheduleGroup {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "group_id")
    var groupId = 0

    @ColumnInfo(name = "color_number")
    var colorNumber: Int
        private set

    @ColumnInfo(name = "group_name")
    var groupName: String
        private set

    @ColumnInfo(name = "character_color")
    var characterColor: String
        private set

    @ColumnInfo(name = "background_color")
    var backgroundColor: Int
        private set

    constructor(colorNumber: Int,
                groupName: String,
                characterColor: String,
                backgroundColor: Int) {
        this.colorNumber = colorNumber
        this.groupName = groupName
        this.characterColor = characterColor
        this.backgroundColor = backgroundColor
    }

    @Ignore
    constructor(groupId: Int,
                colorNumber: Int,
                groupName: String,
                characterColor: String,
                backgroundColor: Int) {
        this.groupId = groupId
        this.colorNumber = colorNumber
        this.groupName = groupName
        this.characterColor = characterColor
        this.backgroundColor = backgroundColor
    }
}