package com.non_name_hero.calenderview.data.source.remote;

import android.content.ContentValues;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.non_name_hero.calenderview.data.Schedule;
import com.non_name_hero.calenderview.data.source.ScheduleDataSource;
import com.non_name_hero.calenderview.utils.AppExecutors;

import java.util.Map;

public class ScheduleDataRemoteSource implements ScheduleDataSource {

    private static volatile ScheduleDataRemoteSource INSTANCE;
    private AppExecutors mAppExecutors;
    private final String PIGLEAD_SCHEDULES = "PigLeadSchedules";
    private final String HOLIDAY_DOCUMENT = "holiday";

    //リモートデータベース
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    public ScheduleDataRemoteSource(@NonNull AppExecutors appExecutors) {
        mAppExecutors = appExecutors;
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
    public void getHoliday(@NonNull final GetScheduleCallback callback) {

//        final List<Schedule> holidaySchedules =
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

                                Log.d(ContentValues.TAG, "DocumentSnapshot data: " + holiday);
                            } else {
                                Log.d(ContentValues.TAG, "No such document");
                            }
                        } else {

                        }
                        Log.d(ContentValues.TAG, "get failed with ", task.getException());
                    }

                });
    }
}

//    private List<Schedule> createSchedules(){
//
//    }
