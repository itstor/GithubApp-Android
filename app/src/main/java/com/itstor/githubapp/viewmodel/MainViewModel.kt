package com.itstor.githubapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.itstor.githubapp.api.ApiConfig
import com.itstor.githubapp.models.SearchResponse
import com.itstor.githubapp.models.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    companion object{
        private const val TAG = "MainViewModel"
    }

    private var _listUserCache: List<UserResponse>? = null

    private val _listUser = MutableLiveData<List<UserResponse>>()
    val listUser: LiveData<List<UserResponse>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>().apply { value = false }
    val isLoading: LiveData<Boolean> = _isLoading

    private val _showToast = MutableLiveData<String?>()
    val showToast: LiveData<String?> = _showToast

    private val perPage: Int = 20

    var searchQuery: String? = null
        set(value) {
            if (value == field) return
            field = value

            getData()
        }

    init {
        getData()
    }

    private fun getData() {
        if (!searchQuery.isNullOrEmpty()) {
            fetchSearchUser(searchQuery!!)
        } else {
            if (_listUserCache.isNullOrEmpty()) return fetchAllUser()

            _listUser.value = _listUserCache!!
        }
    }

    private fun fetchAllUser() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getAllUsers(perPage)

        client.enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(
                call: Call<List<UserResponse>>,
                response: Response<List<UserResponse>>
            ) {
                if (response.isSuccessful) {
                    _listUserCache = response.body()
                    _listUser.value = response.body()
                } else {
                    _showToast.value = "Something went wrong"
                }

                _isLoading.value = false
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                Log.e(TAG, "onFailure fetchAllUser: ${t.message}")
                _showToast.value = t.message
                _isLoading.value = false
            }
        })
    }

    private fun fetchSearchUser(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearchUser(query)

        client.enqueue(object : Callback<SearchResponse<UserResponse>> {
            override fun onResponse(
                call: Call<SearchResponse<UserResponse>>,
                response: Response<SearchResponse<UserResponse>>
            ) {
                if (response.isSuccessful) {
                    _listUser.value = response.body()?.items
                } else {
                    _showToast.value = "Something went wrong"
                }

                _isLoading.value = false
            }

            override fun onFailure(call: Call<SearchResponse<UserResponse>>, t: Throwable) {
                Log.e(TAG, "onFailure fetchSearchUser: ${t.message}")
                _showToast.value = t.message
                _isLoading.value = false
            }
        })
    }
}