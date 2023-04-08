package com.itstor.githubapp.data.source.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.itstor.githubapp.interfaces.SimpleUserInterface
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponse(
    @field:SerializedName("gists_url")
    val gistsUrl: String,

    @field:SerializedName("repos_url")
    val reposUrl: String,

    @field:SerializedName("following_url")
    val followingUrl: String,

    @field:SerializedName("starred_url")
    val starredUrl: String,

    @field:SerializedName("login")
    override val login: String,

    @field:SerializedName("followers_url")
    val followersUrl: String,

    @field:SerializedName("type")
    override val type: String,

    @field:SerializedName("url")
    val url: String,

    @field:SerializedName("subscriptions_url")
    val subscriptionsUrl: String,

    @field:SerializedName("received_events_url")
    val receivedEventsUrl: String,

    @field:SerializedName("avatar_url")
    override val avatarUrl: String,

    @field:SerializedName("events_url")
    val eventsUrl: String,

    @field:SerializedName("html_url")
    val htmlUrl: String,

    @field:SerializedName("site_admin")
    val siteAdmin: Boolean,

    @field:SerializedName("id")
    override val id: Int,

    @field:SerializedName("gravatar_id")
    val gravatarId: String,

    @field:SerializedName("node_id")
    val nodeId: String,

    @field:SerializedName("organizations_url")
    val organizationsUrl: String
) : Parcelable, SimpleUserInterface
