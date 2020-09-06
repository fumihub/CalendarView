package com.non_name_hero.calenderview.utils;

import android.content.Context;

import androidx.annotation.NonNull;

import com.non_name_hero.calenderview.data.source.ScheduleRepository;
import com.non_name_hero.calenderview.data.source.local.PigLeadDatabase;
import com.non_name_hero.calenderview.data.source.local.ScheduleDataLocalSource;
import com.non_name_hero.calenderview.data.source.remote.ScheduleDataRemoteSource;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class Injection {
    public static ScheduleRepository provideScheduleRepository(@NonNull Context context) {
        checkNotNull(context);
        PigLeadDatabase pigLeadDatabase = PigLeadDatabase.getInstance(context);
        AppExecutors appExecutors = new AppExecutors();

        ScheduleRepository scheduleRepository = ScheduleRepository.getInstance(
                ScheduleDataLocalSource.getInstance(
                        appExecutors,
                        pigLeadDatabase.scheduleDao()),
                ScheduleDataRemoteSource.getInstance(appExecutors));
        return scheduleRepository;
    }
}
