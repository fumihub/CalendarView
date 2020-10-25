package com.non_name_hero.calenderview.utils

import android.annotation.SuppressLint
import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.non_name_hero.calenderview.calendar.CalendarViewModel
import com.non_name_hero.calenderview.data.source.ScheduleRepository

class ViewModelFactory private constructor(private val mScheduleRepository: ScheduleRepository?) : NewInstanceFactory() {
    fun getScheduleRepository(): ScheduleRepository? {
        return mScheduleRepository
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalendarViewModel::class.java)) {
            return CalendarViewModel(mScheduleRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        fun getInstance(application: Application): ViewModelFactory? {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = ViewModelFactory(
                                Injection.provideScheduleRepository(application.applicationContext))
                    }
                }
            }
            return INSTANCE
        }

        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}