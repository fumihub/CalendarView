package com.non_name_hero.calenderview.utils

import android.content.Context
import com.google.android.gms.common.internal.Preconditions
import com.non_name_hero.calenderview.data.source.ScheduleRepository
import com.non_name_hero.calenderview.data.source.local.PigLeadDatabase.Companion.getInstance
import com.non_name_hero.calenderview.data.source.local.ScheduleDataLocalSource
import com.non_name_hero.calenderview.data.source.remote.ScheduleDataRemoteSource

object Injection {
    fun provideScheduleRepository(context: Context): ScheduleRepository {
        Preconditions.checkNotNull(context)
        val pigLeadDatabase = getInstance(context)
        val appExecutors = AppExecutors()
        return ScheduleRepository.getInstance(
                ScheduleDataRemoteSource(),
                ScheduleDataLocalSource(
                        AppExecutors(),
                        pigLeadDatabase.scheduleDao()))
    }
}