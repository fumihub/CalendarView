package com.non_name_hero.calenderview.data.source.remote;

import android.content.ContentValues;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.non_name_hero.calenderview.data.CalendarData;
import com.non_name_hero.calenderview.data.Schedule;
import com.non_name_hero.calenderview.data.ScheduleGroup;
import com.non_name_hero.calenderview.data.source.ScheduleDataSource;
import com.non_name_hero.calenderview.utils.AppExecutors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ScheduleDataRemoteSource implements ScheduleDataSource {

    private static volatile ScheduleDataRemoteSource INSTANCE;
    private AppExecutors mAppExecutors;
    private final String PIGLEAD_SCHEDULES = "PigLeadSchedules";
    private final String HOLIDAY_DOCUMENT = "holiday";

    //リモートデータベース
    private FirebaseFirestore db;

    public ScheduleDataRemoteSource(@NonNull AppExecutors appExecutors) {
        mAppExecutors = appExecutors;
        db = FirebaseFirestore.getInstance();
    }

    public static ScheduleDataRemoteSource getInstance(@NonNull AppExecutors appExecutors) {
        if (INSTANCE == null) {
            synchronized (ScheduleDataRemoteSource.class) {
                INSTANCE = new ScheduleDataRemoteSource(appExecutors);
            }
        }
        return INSTANCE;
    }

    @Override
    public void getSchedule(@NonNull long[] ScheduleIds, @NonNull GetScheduleCallback callback) {
        //リモートデータソースは使用しない
    }

    @Override
    public void setSchedule(Schedule schedule, @NonNull SaveScheduleCallback callback) {
        //リモートデータソースは使用しない
    }

    @Override
    public void getAllSchedules(@NonNull GetScheduleCallback callback) {
        //リモートデータソースは使用しない
    }

    @Override
    public void removeScheduleByScheduleId(@NonNull long scheduleId) {

    }

    @Override
    public void getHoliday(@NonNull final LoadHolidayCalendarDataCallback callback) {
        //以下は別スレッドにて実行
        db.collection(PIGLEAD_SCHEDULES)
                .document(HOLIDAY_DOCUMENT)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            //documentにドキュメントデータがあればtrue
                            if (document.exists()) {
                                Map<String, Object> holiday = document.getData();//documentからholidayNameのバリューを取得
                                //受け取ったデータを整形
                                final List<CalendarData> holidaySchedules = new ArrayList<CalendarData>();
                                for(Object obj :holiday.values()){
                                    Map<String,Object> holidayData = autoCast(obj);
                                    Long date = ((Timestamp)holidayData.get("date")).getSeconds()*1000;
                                    CalendarData data = new CalendarData();
                                    data.scheduleTitle = (String)holidayData.get("nameInJapan");
                                    data.scheduleStartAtDatetime = new Date(date);
                                    data.isHoliday = true;
                                    holidaySchedules.add(data);
                                }
                                //callbackに引数を渡す(データ配列)
                                callback.onHolidayCalendarDataLoaded(holidaySchedules);
                                Log.d(ContentValues.TAG, holiday.toString());
                            } else {
                                Log.d(ContentValues.TAG, "No such document");
                            }
                        } else {
                            Log.d(ContentValues.TAG, "failure task firebase");
                        }
                        Log.d(ContentValues.TAG, "get failed with ", task.getException());
                    }
                });
    }

    public void insertScheduleGroup(@NonNull ScheduleGroup group, @NonNull SaveScheduleGroupCallback callback) {

    }

    @Override
    public void deleteScheduleGroup(@NonNull int groupId) {

    }

    @Override
    public void getScheduleGroup(@NonNull int colorNumber, @NonNull GetScheduleGroupCallback callback) {

    }

    @Override
    public void getListScheduleGroup(@NonNull GetScheduleGroupsCallback callback) {

    }

    @Override
    public void updateScheduleGroup(@NonNull ScheduleGroup group, @NonNull SaveScheduleGroupCallback callback) {

    }

    @Override
    public void getCalendarDataList(@NonNull LoadCalendarDataCallback callback) {

    }


    @SuppressWarnings("unchecked")
    public <T> T autoCast(Object obj) {
        T castObj = (T) obj;
        return castObj;
    }
}
