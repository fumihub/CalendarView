package com.non_name_hero.calenderview.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

/**
AppCompatActivityにおける拡張関数を定義する
 **/
/**
 * アクティビティからViewModelを生成する拡張関数
 * @return ViewModel
 */
fun <T : ViewModel> AppCompatActivity.obtainViewModel(viewModelClass: Class<T>) =
        ViewModelProviders.of(this, ViewModelFactory.getInstance(application)).get(viewModelClass)