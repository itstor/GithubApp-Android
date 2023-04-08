package com.itstor.githubapp.ui.view.splash

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.itstor.githubapp.data.SettingPreferences
import com.itstor.githubapp.ui.view.main.SplashViewModel

class SplashViewModelFactory private constructor(
    private val mSettingPreferences: SettingPreferences
) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var INSTANCE: SplashViewModelFactory? = null

        @JvmStatic
        fun getInstance(settingPreferences: SettingPreferences): SplashViewModelFactory {
            if (INSTANCE == null) {
                synchronized(SplashViewModelFactory::class.java) {
                    INSTANCE = SplashViewModelFactory(settingPreferences)
                }
            }
            return INSTANCE as SplashViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(mSettingPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}