package com.non_name_hero.calenderview.data.source.local

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.non_name_hero.calenderview.data.*

/**
 * The Room Database that contains the Task table.
 */
@Database(entities = [Schedule::class, ScheduleGroup::class, Balance::class, BalanceCategory::class, Category::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class PigLeadDatabase : RoomDatabase() {
    abstract fun scheduleDao(): SchedulesDao
    override fun clearAllTables() {}

    companion object {
        private var INSTANCE: PigLeadDatabase? = null
        private val sLock = Any()
        @JvmStatic
        fun getInstance(context: Context): PigLeadDatabase {
            synchronized(sLock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            PigLeadDatabase::class.java, "PigLead.db")
                            .addCallback(object : Callback() {
                                override fun onCreate(db: SupportSQLiteDatabase) {
                                    super.onCreate(db)
                                    /*色グループ用データベース初期値（groupID, colorNumber, text, textColor, color）*/
                                    db.execSQL("INSERT INTO schedule_group VALUES"
                                            + "(1, 43, '未分類', '白', -9404272)")/*灰色*/

                                    /*Category用データベース初期値（categoryID, categoryColor, imgURL, bigCategoryName, balanceType）*/
                                    /*収入*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(1, -16728065,'income_icon', '収入', 1)")/*水色*/
                                    /*費用*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(2, -1027705,'eat_icon', '食費', 0)")/*明るい赤色*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(3, -65281,'train_icon', '交通費', 0)")/*赤紫色*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(4, -15348,'outdoor_icon', '遊び', 0)")/*黄土色*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(5, -103155,'hobby_icon', '趣味', 0)")/*濃いオレンジ色*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(6, -2040,'clothes_icon', '衣服', 0)")/*黄色*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(7, -17443,'beauty_icon', '化粧品', 0)")/*パステルピンク*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(8, -1120744,'unisex_beauty_parlor_icon', '理美容院', 0)")/*ライム色*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(9, -5579350,'smartphone_icon', '通信費', 0)")/*パステル緑色*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(10, -5374161,'dairy_icon', '日用品', 0)")/*黄緑色*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(11, -69,'fitness_icon', '健康', 0)")/*パステル黄色*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(12, -6693377,'tap_icon', '水道・光熱費', 0)")/*パステル青色*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(13, -956798,'mtg_icon', '交際費', 0)")/*朱色*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(14, -7876885,'study_icon', '教養・教育費', 0)")/*暗い水色*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(15, -2232662,'hospital_icon', '医療費', 0)")/*パステル黄緑*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(16, -1146130,'car_icon', '自動車', 0)")/*薄い赤紫色*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(17, -16713062,'house_icon', '住宅', 0)")/*明るい緑*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(18, -551896,'insurance_icon', '保険', 0)")/*薄いオレンジ*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(19, -4431586,'money_icon', '税・社会保障', 0)")/*こげちゃ色*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(20, -16711681,'furniture_icon', '特別な出費', 0)")/*明るい青色*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(21, 0,'other_icon', 'その他', 0)")/*白色*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(22, 0,'unclassified_icon', '未分類', 0)")/*白色*/

                                    /*BalanceCategory用データベース初期値（balanceCategoryId, editableFlg, categoryName, categoryID）*/
                                    /*収入*/
                                    db.execSQL("INSERT INTO balance_category VALUES"
                                            + "(1, 0, '給与', 1)")
                                    db.execSQL("INSERT INTO balance_category VALUES"
                                            + "(2, 0, '一時所得', 1)")
                                    db.execSQL("INSERT INTO balance_category VALUES"
                                            + "(3, 0, '事業・副業', 1)")
                                    db.execSQL("INSERT INTO balance_category VALUES"
                                            + "(4, 0, '年金', 1)")
                                    db.execSQL("INSERT INTO balance_category VALUES"
                                            + "(5, 0, '配当所得', 1)")
                                    db.execSQL("INSERT INTO balance_category VALUES"
                                            + "(6, 0, '不動産所得', 1)")
                                    db.execSQL("INSERT INTO balance_category VALUES"
                                            + "(7, 0, '不明な入金', 1)")
                                    db.execSQL("INSERT INTO balance_category VALUES"
                                            + "(8, 0, 'その他入金', 1)")
                                    /*費用*/
                                    db.execSQL("INSERT INTO balance_category VALUES"
                                            + "(9, 0, '食費', 2)")
                                    db.execSQL("INSERT INTO balance_category VALUES"
                                            + "(10, 0, '交通費', 3)")
                                    db.execSQL("INSERT INTO balance_category VALUES"
                                            + "(11, 0, '遊び', 4)")
                                    db.execSQL("INSERT INTO balance_category VALUES"
                                            + "(12, 0, '趣味', 5)")
                                    db.execSQL("INSERT INTO balance_category VALUES"
                                            + "(13, 0, '衣服', 6)")
                                    db.execSQL("INSERT INTO balance_category VALUES"
                                            + "(14, 0, '化粧品', 7)")
                                    db.execSQL("INSERT INTO balance_category VALUES"
                                            + "(15, 0, '理美容院', 8)")
                                    db.execSQL("INSERT INTO balance_category VALUES"
                                            + "(16, 0, '通信費', 9)")
                                    db.execSQL("INSERT INTO balance_category VALUES"
                                            + "(17, 0, '日用品', 10)")
                                    db.execSQL("INSERT INTO balance_category VALUES"
                                            + "(18, 0, '健康', 11)")
                                    db.execSQL("INSERT INTO balance_category VALUES"
                                            + "(19, 0, '水道・光熱費', 12)")
                                    db.execSQL("INSERT INTO balance_category VALUES"
                                            + "(20, 0, '交際費', 13)")
                                    db.execSQL("INSERT INTO balance_category VALUES"
                                            + "(21, 0, '教養・教育費', 14)")
                                    db.execSQL("INSERT INTO balance_category VALUES"
                                            + "(22, 0, '医療費', 15)")
                                    db.execSQL("INSERT INTO balance_category VALUES"
                                            + "(23, 0, '自動車', 16)")
                                    db.execSQL("INSERT INTO balance_category VALUES"
                                            + "(24, 0, '住宅', 17)")
                                    db.execSQL("INSERT INTO balance_category VALUES"
                                            + "(25, 0, '保険', 18)")
                                    db.execSQL("INSERT INTO balance_category VALUES"
                                            + "(26, 0, '税・社会保障', 19)")
                                    db.execSQL("INSERT INTO balance_category VALUES"
                                            + "(27, 0, '特別な出費', 20)")
                                    db.execSQL("INSERT INTO balance_category VALUES"
                                            + "(28, 0, 'その他', 21)")
                                    db.execSQL("INSERT INTO balance_category VALUES"
                                            + "(29, 0, '未分類', 22)")

                                }
                            }).fallbackToDestructiveMigration()
                            .build()
                }
                return INSTANCE!!
            }
        }
    }
}
