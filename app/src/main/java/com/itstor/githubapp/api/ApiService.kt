package com.itstor.githubapp.api

import com.itstor.githubapp.models.SearchResponse
import com.itstor.githubapp.models.UserDetailResponse
import com.itstor.githubapp.models.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
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