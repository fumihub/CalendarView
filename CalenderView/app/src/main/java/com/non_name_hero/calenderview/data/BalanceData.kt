package com.non_name_hero.calenderview.data

import androidx.room.Ignore
import java.util.*

class BalanceData {

    var balanceId: Long = 0

    var title: String? = null

    var usedAtDatetime: Date? = null

    var price: Long = 0

    var bigCategoryName: String? = null

    var categoryName: String? = null

    var categoryColor: Int = 0

    var imageUrl: String? = null

    @Ignore
    var kind = 0
}