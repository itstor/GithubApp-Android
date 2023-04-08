package com.itstor.githubapp.ui.view.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.itstor.githubapp.data.SettingPreferences

class MainViewModelFactory private constructor(
    private val mApplication: Application,
    private val mSettingPreferences: SettingPreferences
) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var INSTANCE: MainViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application, settingPreferences: SettingPreferences): MainViewModelFactory {
            if (INSTANCE == null) {
                synchronized(MainViewModelFactory::class.java) {
                    INSTANCE = MainViewModelFactory(application, settingPreferences)
                }
            }
            return INSTANCE as MainViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(mApplication, mSettingPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}