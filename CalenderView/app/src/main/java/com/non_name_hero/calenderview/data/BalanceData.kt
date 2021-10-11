package com.non_name_hero.calenderview.data

import androidx.room.Ignore
import com.non_name_hero.calenderview.utils.BalanceType
import java.util.*

class BalanceData {

    var timestamp: String = ""

    var balanceType: BalanceType = BalanceType.EXPENSES

    var price: Long = 0

    var count: Int = 0

    @Ignore
    var priceText: String = ""
        get() {
            return String.format("%,d円", this.price)
        }
}