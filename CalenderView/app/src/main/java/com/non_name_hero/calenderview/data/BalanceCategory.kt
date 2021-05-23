package com.non_name_hero.calenderview.data

import androidx.room.*

@Entity(tableName = "balance_category", indices = [Index(value = ["balance_category_id", "editable_flg"], unique = true)])
class BalanceCategory {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "balance_category_id")
    var balanceCategoryId = 0

    @ColumnInfo(name = "editable_flg")
    var editableFlg = false

    @ColumnInfo(name = "category_name")
    var categoryName: String
        private set

    @ColumnInfo(name = "category_id")
    var categoryID: Int
        private set

    constructor(balanceCategoryId: Int,
                editableFlg: Boolean,
                categoryName: String,
                categoryID: Int) {
        this.balanceCategoryId = balanceCategoryId
        this.editableFlg = editableFlg
        this.categoryName = categoryName
        this.categoryID = categoryID
    }

}