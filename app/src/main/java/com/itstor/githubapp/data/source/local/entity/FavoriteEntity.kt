package com.itstor.githubapp.data.source.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itstor.githubapp.interfaces.SimpleUserInterface
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favorite_user")
@Parcelize
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    override var id: Int = 0,

    @ColumnInfo(name = "login")
    override var login: String = "",

    @ColumnInfo(name = "avatar_url")
    override var avatarUrl: String? = null,

    @ColumnInfo(name = "type")
    override var type: String? = null,
) : Parcelable, SimpleUserInterface