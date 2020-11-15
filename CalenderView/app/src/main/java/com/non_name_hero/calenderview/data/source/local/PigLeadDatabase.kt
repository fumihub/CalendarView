package com.non_name_hero.calenderview.data.source.local

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.non_name_hero.calenderview.data.Schedule
import com.non_name_hero.calenderview.data.ScheduleGroup

/**
 * The Room Database that contains the Task table.
 */
@Database(entities = [Schedule::class, ScheduleGroup::class], version = 2, exportSchema = false)
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
                                    val sql = ("INSERT INTO schedule_group VALUES"
                                            + "(1, 43, '未分類', '白', -9404272)")
                                    db.execSQL(sql)
                                }
                            }).fallbackToDestructiveMigration()
                            .build()
                }
                return INSTANCE!!
            }
        }
    }
}