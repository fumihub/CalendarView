package com.non_name_hero.calenderview.inputForm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SubCategorySelectViewModel: ViewModel() {

    /**
     * 現在のカテゴリ編集モード
     */
    private val _currentEditSubCategoryMode = MutableLiveData<Boolean>().apply { this.value = false }
    val currentEditSubCategoryMode: LiveData<Boolean>
        get() = _currentEditSubCategoryMode

    fun setCurrentEditSubCategoryMode(mode: Boolean) {
        this._currentEditSubCategoryMode.value = mode
    }
}