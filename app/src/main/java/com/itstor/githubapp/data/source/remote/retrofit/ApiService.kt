package com.itstor.githubapp.data.source.remote.retrofit

import com.itstor.githubapp.data.source.remote.response.SearchResponse
import com.itstor.githubapp.data.source.remote.response.UserDetailResponse
import com.itstor.githubapp.data.source.remote.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    fun getAllUsers(
        @Query("per_page") perPage: Int?
    ): Call<List<UserResponse>>

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    fun getAllUserFollower(
        @Path("username") username: String
    ): Call<List<UserResponse>>

    @GET("users/{username}/following")
    fun getAllUserFollowing(
        @Path("username") username: String
    ): Call<List<UserResponse>>

    @GET("search/users")
    fun getSearchUser(
        @Query("q") username: String
    ): Call<SearchResponse<UserResponse>>
}