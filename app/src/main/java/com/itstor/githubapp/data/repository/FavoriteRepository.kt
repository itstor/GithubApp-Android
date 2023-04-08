package com.itstor.githubapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.itstor.githubapp.data.source.local.entity.FavoriteEntity
import com.itstor.githubapp.data.source.local.room.FavoriteDao
import com.itstor.githubapp.data.source.local.room.FavoriteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mFavoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        mFavoriteDao = db.favoriteUserDao()
    }

    fun getAllFavorites(): LiveData<List<FavoriteEntity>> = mFavoriteDao.getAll()

    fun insert(note: FavoriteEntity) {
        executorService.execute { mFavoriteDao.insert(note) }
    }

    fun delete(note: FavoriteEntity) {
        executorService.execute { mFavoriteDao.delete(note) }
    }

    fun update(note: FavoriteEntity) {
        executorService.execute { mFavoriteDao.update(note) }
    }

    fun deleteById(id: Int) {
        executorService.execute { mFavoriteDao.deleteById(id) }
    }

    fun getFavoriteById(id: Int): LiveData<FavoriteEntity> = mFavoriteDao.getFavoriteById(id)

    fun getFavoriteByUsername(username: String): LiveData<FavoriteEntity> = mFavoriteDao.getFavoriteByUsername(username)

    fun countByUserId(id: Int): LiveData<Int> = mFavoriteDao.countById(id)
}