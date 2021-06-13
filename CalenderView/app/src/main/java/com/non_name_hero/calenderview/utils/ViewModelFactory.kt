package com.non_name_hero.calenderview.utils

import android.annotation.SuppressLint
import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.non_name_hero.calenderview.calendar.CalendarViewModel
import com.non_name_hero.calenderview.data.source.ScheduleRepository
import com.non_name_hero.calenderview.inputForm.ColorSelectViewModel
import com.non_name_hero.calenderview.inputForm.SubCategorySelectViewModel

class ViewModelFactory private constructor(private val mScheduleRepository: ScheduleRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        with(modelClass) {
            when{
                isAssignableFrom(CalendarViewModel::class.java) ->
                    CalendarViewModel(mScheduleRepository)
                isAssignableFrom(ColorSelectViewModel::class.java) ->
                    ColorSelectViewModel()
                isAssignableFrom(SubCategorySelectViewModel::class.java) ->
                    SubCategorySelectViewModel()
                else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
        } as T


    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        fun getInstance(application: Application) =
                INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                    INSTANCE ?: ViewModelFactory(
                            Injection.provideScheduleRepository(application.applicationContext))
                            .also { INSTANCE = it }
                }

    }

    @VisibleForTesting
    fun destroyInstance() {
        INSTANCE = null
    }
}