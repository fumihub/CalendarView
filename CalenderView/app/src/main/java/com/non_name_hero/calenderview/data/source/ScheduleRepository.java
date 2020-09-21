package com.non_name_hero.calenderview.data.source;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.non_name_hero.calenderview.data.CalendarData;
import com.non_name_hero.calenderview.data.Schedule;
import com.non_name_hero.calenderview.data.ScheduleGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static androidx.core.util.Preconditions.checkNotNull;


public class ScheduleRepository implements ScheduleDataSource {

    private static ScheduleRepository INSTANCE = null;
    private final ScheduleDataSource mScheduleDataLocalSource;
    private final ScheduleDataSource mScheduleDataRemoteSource;

    List<CalendarData> mCachedHolidayCalenderData;
    List<CalendarData> mCachedCalendarData;
    Map<String, List<Schedule>> mCachedScheduleMap;
    Map<String, List<CalendarData>> mCachedCalendarDataMap;

    boolean mCacheIsDirty = false;
    boolean mCalendarCacheIsDirty = false;
    boolean mHolidayCacheIsDirty = false;

    //コンストラクタ
    @SuppressLint("RestrictedApi")
    private ScheduleRepository(@NonNull ScheduleDataSource ScheduleLocalDataSource,
                               @NonNull ScheduleDataSource ScheduleRemoteDataSource) {
        mScheduleDataLocalSource = checkNotNull(ScheduleLocalDataSource);
        mScheduleDataRemoteSource = checkNotNull(ScheduleRemoteDataSource);
    }

    /**
     * シングルトン、インスタンスの返却.
     *
     * @param scheduleDataLocalSource  デバイス上のストレージ(ローカルデータソース)
     * @param scheduleDataRemoteSource リモートのデータソース
     * @return the {@link ScheduleRepository} リポジトリのインスタンス
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
     * スケジュールIDを指定して情報を取得
     *
     * @param scheduleId 情報を取得したいスケジュールID
     * @param callback   情報取得後の処理
     */
    @SuppressLint("RestrictedApi")
    @Override
    public void getSchedule(@NonNull long[] scheduleId, @NonNull GetScheduleCallback callback) {
        checkNotNull(scheduleId);
        checkNotNull(callback);
        mScheduleDataLocalSource.getSchedule(scheduleId, callback);
    }

    /**
     * スケジュール情報をDBに格納する
     *
     * @param schedule 格納するスケジュールオブジェクト
     * @param callback 格納後の処理
     */
    @SuppressLint("RestrictedApi")
    @Override
    public void setSchedule(Schedule schedule, @NonNull SaveScheduleCallback callback) {
        checkNotNull(schedule);
        mScheduleDataLocalSource.setSchedule(schedule, callback);
    }

    /**
     * 全てのスケジュールデータを取得
     *
     * @param callback
     */
    @Override
    public void getAllSchedules(@NonNull GetScheduleCallback callback) {
        mScheduleDataLocalSource.getAllSchedules(callback);
    }

    public void holidayCacheClear() {
        mHolidayCacheIsDirty = false;
        mCachedHolidayCalenderData = null;
    }

    /**
     * 祝日データを取得
     *
     * @param callback 取得後の処理
     */
    @Override
    public void getHoliday(@NonNull final LoadHolidayCalendarDataCallback callback) {
        if (mCachedHolidayCalenderData == null && mHolidayCacheIsDirty == false) {
            mCachedHolidayCalenderData = new ArrayList<>();
            mScheduleDataRemoteSource.getHoliday(new LoadHolidayCalendarDataCallback() {
                @Override
                public void onHolidayCalendarDataLoaded(List<CalendarData> calendarDataList) {
                    //キャッシュを保持
                    mCachedHolidayCalenderData = calendarDataList;
                    callback.onHolidayCalendarDataLoaded(calendarDataList);
                }

                @Override
                public void onDataNotAvailable() {
                    callback.onDataNotAvailable();
                }
            });
        } else {
            callback.onHolidayCalendarDataLoaded(mCachedHolidayCalenderData);
        }
    }

    public void scheduleCacheClear() {
        mCacheIsDirty = false;
        mCachedScheduleMap = null;
    }

    /**
     * グループ情報DBに追加する
     *
     * @param group    グループオブジェクト
     * @param callback 　保存完了後の処理、保存失敗時の処理
     */
    @Override
    public void insertScheduleGroup(@NonNull ScheduleGroup group, @NonNull SaveScheduleGroupCallback callback) {
        mScheduleDataLocalSource.insertScheduleGroup(group, callback);
    }

    /**
     * colorNumberを指定してグループ情報を削除
     *
     * @param colorNumber カラー番号
     */
    @Override
    public void deleteScheduleGroup(@NonNull int colorNumber) {
        mScheduleDataLocalSource.deleteScheduleGroup(colorNumber);
    }

    /**
     * colorNumberを指定してグループ情報を取得
     *
     * @param colorNumber 　カラー番号
     * @param callback    取得後の処理。引数に取得した情報をとる
     */
    @Override
    public void getScheduleGroup(@NonNull int colorNumber, @NonNull GetScheduleGroupCallback callback) {
        mScheduleDataLocalSource.getScheduleGroup(colorNumber, callback);
    }

    /**
     * グループ情報を全権取得
     *
     * @param callback - onScheduleGroupsLoaded(List<ScheduleGroup> scheduleGroups) 情報取得後の処理。引数に全件グループ情報を保持
     */
    @Override
    public void getListScheduleGroup(@NonNull GetScheduleGroupsCallback callback) {
        mScheduleDataLocalSource.getListScheduleGroup(callback);
    }

    /**
     * スケジュールグループを更新
     * primaryを合わせる
     * @param group 更新対象のスケジュールグループ
     * @param callback コールバック
     */
    @Override
    public void updateScheduleGroup(@NonNull ScheduleGroup group, @NonNull SaveScheduleGroupCallback callback) {
        mScheduleDataLocalSource.updateScheduleGroup(group, callback);
    }

    public void calendarCacheClear() {
        mCachedCalendarData= null;
        mCalendarCacheIsDirty = false;
    }
    /**
     * カレンダー表示データを取得
     * @param callback コールバック
     */
    @Override
    public void getCalendarDataList(@NonNull final LoadCalendarDataCallback callback) {
        if (mCachedCalendarData == null && mCalendarCacheIsDirty == false) {
            mCachedCalendarData = new ArrayList<>();
            mScheduleDataLocalSource.getCalendarDataList(new LoadCalendarDataCallback() {
                @Override
                public void onCalendarDataLoaded(List<CalendarData> calendarDataList) {
                    mCacheIsDirty = true;
                    callback.onCalendarDataLoaded(calendarDataList);
                }

                @Override
                public void onDataNotAvailable() {
                    callback.onDataNotAvailable();
                }
            });
        } else {
            mScheduleDataRemoteSource.getCalendarDataList(callback);
        }
    }

}
