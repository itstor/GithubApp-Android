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

class MainViewModel(application: Application, private val pref: SettingPreferences) : ViewModel() {
    private val mFavoriteRepository = FavoriteRepository(application)

    private val mListAllUsers = MutableLiveData<List<UserResponse>>()
    val listAllUsers: LiveData<List<UserResponse>> = mListAllUsers

    private val mListSearchUser = MutableLiveData<List<UserResponse>>()
    val listSearchUser: LiveData<List<UserResponse>> = mListSearchUser

    private val mIsLoadingAllUser = MutableLiveData<Boolean>().apply { value = false }
    val isLoadingAllUser: LiveData<Boolean> = mIsLoadingAllUser

    private val mIsLoadingSearchUser = MutableLiveData<Boolean>().apply { value = false }
    val isLoadingSearchUser: LiveData<Boolean> = mIsLoadingSearchUser

    private val mShowToast = MutableLiveData<String?>()
    val showToast: LiveData<String?> = mShowToast

    private val perPage: Int = 20

    var searchQuery: String? = null
        set(value) {
            if (value == field) return
            field = value

            if (!value.isNullOrEmpty()) {
                fetchSearchUser(value)
            } else {
                mListSearchUser.value = emptyList()
            }
        }

    init {
        fetchAllUser()
    }

    private fun fetchAllUser() {
        mIsLoadingAllUser.value = true
        val client = ApiConfig.getApiService().getAllUsers(perPage)

        client.enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(
                call: Call<List<UserResponse>>,
                response: Response<List<UserResponse>>
            ) {
                if (response.isSuccessful) {
                    mListAllUsers.value = response.body()
                } else {
                    mShowToast.value = "Something went wrong"
                }

                mIsLoadingAllUser.value = false
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                Log.e(TAG, "onFailure fetchAllUser: ${t.message}")
                mShowToast.value = t.message
                mIsLoadingAllUser.value = false
            }
        })
    }

    private fun fetchSearchUser(query: String) {
        mIsLoadingSearchUser.value = true
        val client = ApiConfig.getApiService().getSearchUser(query)

        client.enqueue(object : Callback<SearchResponse<UserResponse>> {
            override fun onResponse(
                call: Call<SearchResponse<UserResponse>>,
                response: Response<SearchResponse<UserResponse>>
            ) {
                if (response.isSuccessful) {
                    mListSearchUser.value = response.body()?.items
                } else {
                    mShowToast.value = "Something went wrong"
                }

                mIsLoadingSearchUser.value = false
            }

            override fun onFailure(call: Call<SearchResponse<UserResponse>>, t: Throwable) {
                Log.e(TAG, "onFailure fetchSearchUser: ${t.message}")
                mShowToast.value = t.message
                mIsLoadingSearchUser.value = false
            }
        })
    }

    fun getFavoriteUser(): LiveData<List<FavoriteEntity>> = mFavoriteRepository.getAllFavorites()

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    suspend fun switchTheme() {
        pref.saveThemeSetting(!pref.getThemeSetting().first())
    }


    companion object{
        private const val TAG = "MainViewModel"
    }
}