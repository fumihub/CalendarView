package com.non_name_hero.calenderview.inputForm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ColorSelectViewModel: ViewModel() {
    /**
     * 現在の色編集モード
     */
    private val _currentEditColorMode = MutableLiveData<Boolean>().apply { this.value = false}
    val currentEditColorMode: LiveData<Boolean>
        get() = _currentEditColorMode

    fun setCurrentEditColorMode(mode: Boolean) {
        this._currentEditColorMode.value = mode
    }
}