package com.non_name_hero.calenderview.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.non_name_hero.calenderview.utils.BalanceType
import com.non_name_hero.calenderview.utils.PigLeadUtils
import java.util.*

@Entity(tableName = "balance")
class Balance(
    balanceId: Long,
    price: Long,
    balanceCategoryId: Int,
    usedAtDatetime: Date,
    title: String
) {

    @ColumnInfo(name = "balance_id")
    @PrimaryKey(autoGenerate = true)
    var balanceId: Long

    @ColumnInfo(name = "price")
    var price: Long

    @ColumnInfo(name = "balance_category_id")
    var balanceCategoryId: Int


    @ColumnInfo(name = "used_at_datetime")
    private var mUsedAtDatetime: Date

    @ColumnInfo(name = "timestamp")
    var timestamp: String = ""

    @ColumnInfo(name = "title")
    var title: String

    @Ignore
    constructor(
        price: Long,
        balanceCategoryId: Int,
        usedAtDatetime: Date,
        title: String
    ) : this(0, price, balanceCategoryId, usedAtDatetime, title) {
    }

    val usedAtDatetime: Date?
        get() = mUsedAtDatetime

    fun setUsedAtDatetime(mUsedAtDatetime: Date) {
        this.mUsedAtDatetime = mUsedAtDatetime
    }

    init {
        this.balanceId = balanceId
        this.price = price
        this.balanceCategoryId = balanceCategoryId
        mUsedAtDatetime = usedAtDatetime
        this.timestamp = PigLeadUtils.formatYYYYMMDD.format(usedAtDatetime)
        this.title = title
    }
}