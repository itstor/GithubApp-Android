package com.itstor.githubapp.helper

import androidx.recyclerview.widget.DiffUtil
import com.itstor.githubapp.data.source.remote.response.UserResponse
import com.itstor.githubapp.interfaces.SimpleUserInterface

class UserDiffCallback(private val mOldUserList: List<SimpleUserInterface>, private val mNewUserList: List<SimpleUserInterface>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldUserList.size
    }
    override fun getNewListSize(): Int {
        return mNewUserList.size
    }
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldUserList[oldItemPosition].id == mNewUserList[newItemPosition].id
    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFavorite = mOldUserList[oldItemPosition]
        val newFavorite = mNewUserList[newItemPosition]
        return oldFavorite.id == newFavorite.id && oldFavorite.login == newFavorite.login
    }
}