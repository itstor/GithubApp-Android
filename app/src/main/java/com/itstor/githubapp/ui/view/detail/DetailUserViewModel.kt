package com.itstor.githubapp.ui.view.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.itstor.githubapp.data.repository.FavoriteRepository
import com.itstor.githubapp.data.source.local.entity.FavoriteEntity
import com.itstor.githubapp.data.source.remote.retrofit.ApiConfig
import com.itstor.githubapp.data.source.remote.response.UserDetailResponse
import com.itstor.githubapp.data.source.remote.response.UserResponse
import com.itstor.githubapp.interfaces.SimpleUserInterface
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application) : ViewModel() {
    private val mFavoriteRepository = FavoriteRepository(application)

    private val mIsLoadingDetail = MutableLiveData<Boolean>().apply { value = false }
    val isLoadingDetail: MutableLiveData<Boolean> = mIsLoadingDetail

    private  val mIsLoadingFollower = MutableLiveData<Boolean>().apply { value = false }
    val isLoadingFollower: MutableLiveData<Boolean> = mIsLoadingFollower

    private val mIsLoadingFollowing = MutableLiveData<Boolean>().apply { value = false }
    val isLoadingFollowing: MutableLiveData<Boolean> = mIsLoadingFollowing

    private val mShowToast = MutableLiveData<String?>()
    val showToast: MutableLiveData<String?> = mShowToast

    private val mListFollower = MutableLiveData<List<UserResponse>>()
    val listFollower: MutableLiveData<List<UserResponse>> = mListFollower

    private val mListFollowing = MutableLiveData<List<UserResponse>>()
    val listFollowing: MutableLiveData<List<UserResponse>> = mListFollowing

    private val mDetailUser = MutableLiveData<UserDetailResponse>()
    val detailUser: MutableLiveData<UserDetailResponse> = mDetailUser

    var isFavorite: Boolean = false

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
        mIsLoadingDetail.value = true
        val client = ApiConfig.getApiService().getUserDetail(username!!)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                mIsLoadingDetail.value = false
                if (response.isSuccessful) {
                    mDetailUser.value = response.body()
                } else {
                    mShowToast.value = "Something went wrong"
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                Log.e(TAG, "onFailure fetchDetailUser: ${t.message}", t)
                mIsLoadingDetail.value = false
                mShowToast.value = t.message
            }
        })
    }

    private fun fetchFollowerUser() {
        mIsLoadingFollower.value = true
        val client = ApiConfig.getApiService().getAllUserFollower(username!!)

        client.enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(
                call: Call<List<UserResponse>>,
                response: Response<List<UserResponse>>
            ) {
                mIsLoadingFollower.value = false
                if (response.isSuccessful) {
                    mListFollower.value = response.body()
                } else {
                    mShowToast.value = "Something went wrong"
                }
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                Log.e(TAG, "onFailure fetchFollowerUser: ${t.message}", t)
                mIsLoadingFollower.value = false
                mShowToast.value = t.message
            }
        })
    }

    private fun fetchFollowingUser() {
        mIsLoadingFollowing.value = true
        val client = ApiConfig.getApiService().getAllUserFollowing(username!!)
        client.enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(
                call: Call<List<UserResponse>>,
                response: Response<List<UserResponse>>
            ) {
                mIsLoadingFollowing.value = false
                if (response.isSuccessful) {
                    mListFollowing.value = response.body()
                } else {
                    mShowToast.value = "Something went wrong"
                }
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                Log.e(TAG, "onFailure fetchFollowingUser: ${t.message}", t)
                mIsLoadingFollowing.value = false
                mShowToast.value = t.message
            }
        })
    }

    fun isUserFavorite(id: Int) : LiveData<Int> {
        return mFavoriteRepository.countByUserId(id)
    }
    fun addToFavorite(user: SimpleUserInterface) {
        val favorite = FavoriteEntity(
            user.id,
            user.login,
            user.avatarUrl,
            user.type
        )
        mFavoriteRepository.insert(favorite)
    }

    fun removeFromFavorite(userId: Int) {
        mFavoriteRepository.deleteById(userId)
    }
}