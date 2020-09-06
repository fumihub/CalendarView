package com.non_name_hero.calenderview.data.source.local;

import androidx.annotation.NonNull;

import com.non_name_hero.calenderview.data.Schedule;
import com.non_name_hero.calenderview.data.ScheduleGroup;
import com.non_name_hero.calenderview.data.source.ScheduleDataSource;
import com.non_name_hero.calenderview.utils.AppExecutors;

import java.util.List;

public class ScheduleDataLocalSource implements ScheduleDataSource {

    private static volatile ScheduleDataLocalSource INSTANCE;

    private SchedulesDao mSchedulesDao;

    private AppExecutors mAppExecutors;

    //コンストラクタ
    private ScheduleDataLocalSource(@NonNull AppExecutors appExecutors,
                                    @NonNull SchedulesDao dao) {
        mSchedulesDao = dao;
        mAppExecutors = appExecutors;
    }

    //シングルトン
    public static ScheduleDataLocalSource getInstance(@NonNull AppExecutors appExecutors,
                                                      @NonNull SchedulesDao usersDao) {
        if (INSTANCE == null) {
            synchronized (ScheduleDataLocalSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ScheduleDataLocalSource(appExecutors, usersDao);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * スケジュールIDの配列からscheduleオブジェクト配列を取得
     *
     * @param scheduleIds
     * @param callback
     */
    @Override
    public void getSchedule(@NonNull final long[] scheduleIds, @NonNull final GetScheduleCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Schedule> schedules = mSchedulesDao.loadSchedulesByIds(scheduleIds);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onScheduleLoaded(schedules);
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }


    @Override
    public void setSchedule(final Schedule schedule, @NonNull final SaveScheduleCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                mSchedulesDao.insertSchedule(schedule);
                //insert完了

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onScheduleSaved();
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getAllSchedules(@NonNull final GetScheduleCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Schedule> schedules = mSchedulesDao.getAll();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onScheduleLoaded(schedules);
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getHoliday(GetScheduleCallback callback) {
        //ローカルデータソースは使用しない
    }

    @Override

    public void getSchedulesMap(GetScheduleMapCallback callback) {

    public void insertScheduleGroup(@NonNull final ScheduleGroup group, @NonNull final SaveScheduleGroupCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mSchedulesDao.insertScheduleGroup(group);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onScheduleGroupSaved();
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteScheduleGroup(@NonNull final int colorNumber) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mSchedulesDao.deleteScheduleGroupByColorNumber(colorNumber);
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getScheduleGroup(@NonNull final int colorNumber, @NonNull final GetScheduleGroupCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final ScheduleGroup group = mSchedulesDao.getScheduleGroupByColorNumber(colorNumber).get(0);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onScheduleGroupLoaded(group);
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getListScheduleGroup(@NonNull final GetScheduleGroupsCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<ScheduleGroup> groups = mSchedulesDao.getAllScheduleGroup();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onScheduleGroupsLoaded(groups);
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }
}
