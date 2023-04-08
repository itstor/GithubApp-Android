package com.itstor.githubapp.ui.view.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.itstor.githubapp.data.SettingPreferences
import com.itstor.githubapp.data.repository.FavoriteRepository
import com.itstor.githubapp.data.source.local.entity.FavoriteEntity
import com.itstor.githubapp.data.source.remote.retrofit.ApiConfig
import com.itstor.githubapp.data.source.remote.response.SearchResponse
import com.itstor.githubapp.data.source.remote.response.UserResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashViewModel(private val pref: SettingPreferences) : ViewModel() {
    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    companion object{
        private const val TAG = "SplashViewModel"
    }
}