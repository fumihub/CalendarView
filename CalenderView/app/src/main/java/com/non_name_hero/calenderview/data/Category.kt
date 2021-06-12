package com.non_name_hero.calenderview.data

import androidx.room.*

@Entity(tableName = "category", indices = [Index(value = ["category_id"], unique = true)])
class Category {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id")
    var categoryId = 0

    @ColumnInfo(name = "category_color")
    var categoryColor: Int

    @ColumnInfo(name = "image_url")
    var imgURL: String
        private set

    @ColumnInfo(name = "big_category_name")
    var bigCategoryName: String
        private set

    constructor(categoryId: Int,
                categoryColor: Int,
                imgURL: String,
                bigCategoryName: String) {
        this.categoryId = categoryId
        this.categoryColor = categoryColor
        this.imgURL = imgURL
        this.bigCategoryName = bigCategoryName
    }

}