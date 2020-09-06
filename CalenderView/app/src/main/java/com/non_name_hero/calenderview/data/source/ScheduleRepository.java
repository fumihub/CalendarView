package com.non_name_hero.calenderview.data.source;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.non_name_hero.calenderview.data.Schedule;
import com.non_name_hero.calenderview.utils.PigLeadUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.core.util.Preconditions.checkNotNull;


public class ScheduleRepository implements ScheduleDataSource {

    private static ScheduleRepository INSTANCE = null;
    private final ScheduleDataSource mScheduleDataLocalSource;
    private final ScheduleDataSource mScheduleDataRemoteSource;

    List<Schedule> mCachedHolidaySchedules;
    Map<String, List<Schedule>> mCachedScheduleMap;

    boolean mCacheIsDirty = false;
    //コンストラクタ
    @SuppressLint("RestrictedApi")
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
    @SuppressLint("RestrictedApi")
    @Override
    public void getSchedule(@NonNull long[] scheduleId, @NonNull GetScheduleCallback callback) {
        checkNotNull(scheduleId);
        checkNotNull(callback);
        mScheduleDataLocalSource.getSchedule(scheduleId, callback);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setSchedule(Schedule schedule, @NonNull SaveScheduleCallback callback) {
        checkNotNull(schedule);
        mScheduleDataLocalSource.setSchedule(schedule, callback);
    }

    @Override
    public void getAllSchedules(@NonNull GetScheduleCallback callback) {
        mScheduleDataLocalSource.getAllSchedules(callback);
    }

    @Override
    public void getHoliday(@NonNull final GetScheduleCallback callback) {
        if (mCachedHolidaySchedules == null){
            mScheduleDataRemoteSource.getHoliday(new GetScheduleCallback() {
                @Override
                public void onScheduleLoaded(List<Schedule> schedules) {
                    //キャッシュを保持
                    mCachedHolidaySchedules = schedules;
                    callback.onScheduleLoaded(mCachedHolidaySchedules);
                }

                @Override
                public void onDataNotAvailable() {

                    callback.onDataNotAvailable();
                }
            });
        }else{
            callback.onScheduleLoaded(mCachedHolidaySchedules);
        }
    }

    public void cacheClear(){
        mCacheIsDirty = false;
        mCachedScheduleMap = null;
    }

    public void getSchedulesMap(@NonNull final GetScheduleMapCallback callback) {
        if (mCachedScheduleMap == null && mCacheIsDirty == false) {
            mCachedScheduleMap = new HashMap<>();
            mScheduleDataLocalSource.getAllSchedules(new GetScheduleCallback() {
                @Override
                public void onScheduleLoaded(List<Schedule> schedules) {
                    mCachedScheduleMap = PigLeadUtils.getScheduleMapBySchedules(schedules);
                    mCacheIsDirty = true;
                    callback.onScheduleMapLoaded(mCachedScheduleMap);
                }

                @Override
                public void onDataNotAvailable() {

                }
            });
        } else {
            callback.onScheduleMapLoaded(mCachedScheduleMap);
        }
    }

}
