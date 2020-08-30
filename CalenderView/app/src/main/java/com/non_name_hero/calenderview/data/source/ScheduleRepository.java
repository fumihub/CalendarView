package com.non_name_hero.calenderview.data.source;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.non_name_hero.calenderview.data.Schedule;
import com.non_name_hero.calenderview.data.ScheduleGroup;

import java.util.List;

import java.util.Map;

import static androidx.core.util.Preconditions.checkNotNull;


public class ScheduleRepository implements ScheduleDataSource {

    private static ScheduleRepository INSTANCE = null;
    private final ScheduleDataSource mScheduleDataLocalSource;
    private final ScheduleDataSource mScheduleDataRemoteSource;

    Map<String, Schedule> mCachedSchedules;
    List<Schedule> mCachedHolidaySchedules;

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
     * @param scheduleId 情報を取得したいスケジュールID
     * @param callback 情報取得後の処理
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
     * @param callback
     */
    @Override
    public void getAllSchedules(@NonNull GetScheduleCallback callback) {
        mScheduleDataLocalSource.getAllSchedules(callback);
    }

    /**
     * 祝日データを取得
     * @param callback 取得後の処理
     */
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

                }
            });
        }else{
            callback.onScheduleLoaded(mCachedHolidaySchedules);
        }
    }

    /**
     * グループ情報DBに追加する
     * @param group グループオブジェクト
     * @param callback　保存完了後の処理、保存失敗時の処理
     */
    @Override
    public void insertScheduleGroup(@NonNull ScheduleGroup group, @NonNull SaveScheduleGroupCallback callback) {
        mScheduleDataLocalSource.insertScheduleGroup(group, callback);
    }

    /**
     * colorNumberを指定してグループ情報を削除
     * @param colorNumber カラー番号
     */
    @Override
    public void deleteScheduleGroup(@NonNull int colorNumber) {
        mScheduleDataLocalSource.deleteScheduleGroup(colorNumber);
    }

    /**
     * colorNumberを指定してグループ情報を取得
     * @param colorNumber　カラー番号
     * @param callback 取得後の処理。引数に取得した情報をとる
     */
    @Override
    public void getScheduleGroup(@NonNull int colorNumber, @NonNull GetScheduleGroupCallback callback) {
        mScheduleDataLocalSource.getScheduleGroup(colorNumber, callback);
    }

    /**
     * グループ情報を全権取得
     * @param callback - onScheduleGroupsLoaded(List<ScheduleGroup> scheduleGroups) 情報取得後の処理。引数に全件グループ情報を保持
     */
    @Override
    public void getListScheduleGroup(@NonNull GetScheduleGroupsCallback callback) {
        mScheduleDataLocalSource.getListScheduleGroup(callback);
    }
}
