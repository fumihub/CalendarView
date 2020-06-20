package com.non_name_hero.calenderview.data.source.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.non_name_hero.calenderview.data.Schedule;


/**
 * The Room Database that contains the Task table.
 */
@Database(entities = {Schedule.class}, version = 1)
public abstract class PigLeadDatabase extends RoomDatabase {
    private static PigLeadDatabase INSTANCE;

    public abstract SchedulesDao scheduleDao();

    private static final Object sLock = new Object();

    public static PigLeadDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        PigLeadDatabase.class, "Tasks.db")
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
