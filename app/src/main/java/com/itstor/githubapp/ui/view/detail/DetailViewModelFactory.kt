package com.itstor.githubapp.ui.view.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.itstor.githubapp.ui.view.detail.DetailUserViewModel

class DetailViewModelFactory private constructor(private val mApplication: Application) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: DetailViewModelFactory? = null
        @JvmStatic
        fun getInstance(application: Application): DetailViewModelFactory {
            if (INSTANCE == null) {
                synchronized(DetailViewModelFactory::class.java) {
                    INSTANCE = DetailViewModelFactory(application)
                }
            }
            return INSTANCE as DetailViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailUserViewModel::class.java)) {
            return DetailUserViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(DetailUserViewModel::class.java)) {
            return DetailUserViewModel(mApplication) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}