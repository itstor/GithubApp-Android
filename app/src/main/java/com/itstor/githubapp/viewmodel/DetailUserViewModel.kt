package com.itstor.githubapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.itstor.githubapp.api.ApiConfig
import com.itstor.githubapp.models.UserDetailResponse
import com.itstor.githubapp.models.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel : ViewModel() {
    private val _isLoadingDetail = MutableLiveData<Boolean>().apply { value = false }
    val isLoadingDetail: MutableLiveData<Boolean> = _isLoadingDetail

    private  val _isLoadingFollower = MutableLiveData<Boolean>().apply { value = false }
    val isLoadingFollower: MutableLiveData<Boolean> = _isLoadingFollower

    private val _isLoadingFollowing = MutableLiveData<Boolean>().apply { value = false }
    val isLoadingFollowing: MutableLiveData<Boolean> = _isLoadingFollowing

    private val _showToast = MutableLiveData<String?>()
    val showToast: MutableLiveData<String?> = _showToast

    private val _listFollower = MutableLiveData<List<UserResponse>>()
    val listFollower: MutableLiveData<List<UserResponse>> = _listFollower

    private val _listFollowing = MutableLiveData<List<UserResponse>>()
    val listFollowing: MutableLiveData<List<UserResponse>> = _listFollowing

    private val _detailUser = MutableLiveData<UserDetailResponse>()
    val detailUser: MutableLiveData<UserDetailResponse> = _detailUser

    companion object {
        private const val TAG = "DetailUserViewModel"
    }

    var username: String? = null
        set(value) {
            if (value == field) return

            field = value

            if (value != null && value.isNotEmpty()) {
                fetchDetailUser()
                fetchFollowerUser()
                fetchFollowingUser()
            }
        }

    private fun fetchDetailUser() {
        _isLoadingDetail.value = true
        val client = ApiConfig.getApiService().getUserDetail(username!!)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                _isLoadingDetail.value = false
                if (response.isSuccessful) {
                    _detailUser.value = response.body()
                } else {
                    _showToast.value = "Something went wrong"
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                Log.e(TAG, "onFailure fetchDetailUser: ${t.message}", t)
                _isLoadingDetail.value = false
                _showToast.value = t.message
            }
        })
    }

    private fun fetchFollowerUser() {
        _isLoadingFollower.value = true
        val client = ApiConfig.getApiService().getAllUserFollower(username!!)

        client.enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(
                call: Call<List<UserResponse>>,
                response: Response<List<UserResponse>>
            ) {
                _isLoadingFollower.value = false
                if (response.isSuccessful) {
                    _listFollower.value = response.body()
                } else {
                    _showToast.value = "Something went wrong"
                }
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                Log.e(TAG, "onFailure fetchFollowerUser: ${t.message}", t)
                _isLoadingFollower.value = false
                _showToast.value = t.message
            }
        })
    }

    private fun fetchFollowingUser() {
        _isLoadingFollowing.value = true
        val client = ApiConfig.getApiService().getAllUserFollowing(username!!)
        client.enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(
                call: Call<List<UserResponse>>,
                response: Response<List<UserResponse>>
            ) {
                _isLoadingFollowing.value = false
                if (response.isSuccessful) {
                    _listFollowing.value = response.body()
                } else {
                    _showToast.value = "Something went wrong"
                }
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                Log.e(TAG, "onFailure fetchFollowingUser: ${t.message}", t)
                _isLoadingFollowing.value = false
                _showToast.value = t.message
            }
        })
    }
}