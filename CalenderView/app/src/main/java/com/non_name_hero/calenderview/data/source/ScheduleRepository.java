package com.non_name_hero.calenderview.data.source;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.non_name_hero.calenderview.data.Schedule;

import java.util.Calendar;
import java.util.Map;

import static com.google.android.gms.internal.ads.zzdlg.checkNotNull;

public class ScheduleRepository implements ScheduleDataSource {

    private static ScheduleRepository INSTANCE = null;
    private final ScheduleDataSource mScheduleDataLocalSource;
    private final ScheduleDataSource mScheduleDataRemoteSource;

    Map<String, Schedule> mCachedSchedules;

    boolean mCacheIsDirty = false;
    //コンストラクタ
    private ScheduleRepository(@NonNull ScheduleDataSource ScheduleLocalDataSource,
                               @NonNull ScheduleDataSource ScheduleRemoteDataSource){
        mScheduleDataLocalSource = checkNotNull(ScheduleLocalDataSource);
        mScheduleDataRemoteSource = checkNotNull(ScheduleRemoteDataSource);
    }

    /**
     * シングルトン、インスタンスの返却.
     *
     * @param scheduleDataLocalSource  the device storage data source
     * @param scheduleDataRemoteSource the backend data source
     * @return the {@link ScheduleRepository} instance
     */
    public static ScheduleRepository getInstance(ScheduleDataSource scheduleDataLocalSource,
                                              ScheduleDataSource scheduleDataRemoteSource) {
        if (INSTANCE == null) {
            INSTANCE = new ScheduleRepository(scheduleDataLocalSource, scheduleDataRemoteSource);
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(ScheduleDataSource, ScheduleDataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * @param scheduleId
     * @param callback
     */
    @Override
    public void getSchedule(@NonNull long[] scheduleId, @NonNull GetScheduleCallback callback) {
        checkNotNull(scheduleId);
        checkNotNull(callback);
        mScheduleDataLocalSource.getSchedule(scheduleId, callback);
    }

    @Override
    public void setSchedule(Schedule schedule) {
        checkNotNull(schedule);
        mScheduleDataLocalSource.setSchedule(schedule);
    }
}