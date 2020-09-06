package com.non_name_hero.calenderview.data.source.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.non_name_hero.calenderview.data.ScheduleGroup;
import com.non_name_hero.calenderview.data.Schedule;


/**
 * The Room Database that contains the Task table.
 */
@Database(entities = {Schedule.class, ScheduleGroup.class}, version = 2, exportSchema = false)
@TypeConverters({Converter.class})
public abstract class PigLeadDatabase extends RoomDatabase {
    private static PigLeadDatabase INSTANCE;

    public abstract SchedulesDao scheduleDao();

    private static final Object sLock = new Object();

    public static PigLeadDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        PigLeadDatabase.class, "PigLead.db")
                        .addCallback(new RoomDatabase.Callback(){
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);
                                String sql = "INSERT INTO schedule_group VALUES"
                                        + "(43, '未分類', '白', -9404272)";
                                db.execSQL(sql);
                            }
                        })

                        .build();
            }
            return INSTANCE;
        }
    }

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}
