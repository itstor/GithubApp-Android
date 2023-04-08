package com.itstor.githubapp.interfaces

import android.os.Parcelable

interface SimpleUserInterface : Parcelable {
    val id: Int
    val login: String
    val avatarUrl: String?
    val type: String?
}