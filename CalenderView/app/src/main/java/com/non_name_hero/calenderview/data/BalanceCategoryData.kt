package com.non_name_hero.calenderview.data

import androidx.room.Ignore
import com.non_name_hero.calenderview.utils.BalanceType

class BalanceCategoryData {

    var timestamp: String = ""

    var balanceType: BalanceType = BalanceType.EXPENSES

    var price: Long = 0

    var categoryName: String = ""

    @Ignore
    var priceText: String = ""
        get() {
            return String.format("%,d円", this.price)
        }

}