package com.non_name_hero.calenderview.data.source.local

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.non_name_hero.calenderview.data.*

/**
 * The Room Database that contains the Task table.
 */
@Database(entities = [Schedule::class, ScheduleGroup::class, Balance::class, BalanceCategory::class, Category::class], version = 2, exportSchema = false)
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
                                    /*Category用データベース初期値（categoryID, categoryColor, imgURL, bigCategoryName）*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(1, -2285516, 'R.drawable.eat_icon', '食費')")/*赤色*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(2, -7722014, 'R.drawable.train_icon', '交通費')")/*紫色*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(3, -15348, 'R.drawable.outdoor_icon', 'アウトドア')")/*黄土色*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(4, -65281, 'R.drawable.hobby_icon', '趣味')")/*赤紫色*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(5, -2040, 'R.drawable.clothes_icon', '衣服')")/*黄色*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(6, -17443, 'R.drawable.beauty_icon', '化粧品')")/*パステルピンク*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(7, -16728065, 'R.drawable.unisex_beauty_parlor_icon', '理美容院')")/*水色*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(8, -16744448, 'R.drawable.smartphone_icon', '通信費')")/*緑色*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(9, -5374161, 'R.drawable.dairy_icon', '日用品')")/*黄緑色*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(10, -36063, 'R.drawable.fitness_icon', '健康')")/*オレンジ色*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(11, -16776961, 'R.drawable.tap_icon', '水道・光熱費')")/*青色*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(12, -956798, 'R.drawable.mtg_icon', '交際費')")/*朱色*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(13, -16777011, 'R.drawable.study_icon', '教養・教育費')")/*藍色*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(14, -2232662, 'R.drawable.hospital_icon', '医療費')")/*パステル黄緑*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(15, -7114533, 'R.drawable.car_icon', '自動車')")/*薄紫色*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(16, -16713062, 'R.drawable.house_icon', '住宅')")/*明るい緑*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(17, -551896, 'R.drawable.insurance_icon', '保険')")/*薄いオレンジ*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(18, -8566223, 'R.drawable.money_icon', '税・社会保障')")/*こげちゃ色*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(19, -16711681, 'R.drawable.furniture_icon', '特別な出費')")/*明るい青色*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(20, 0, 'R.drawable.other_icon', 'その他')")/*白色*/
                                    db.execSQL("INSERT INTO category VALUES"
                                            + "(21, 0, 'R.drawable.unclassified_icon', '未分類')")/*白色*/

                                }
                            }).fallbackToDestructiveMigration()
                            .build()
                }
                return INSTANCE!!
            }
        }
    }
}