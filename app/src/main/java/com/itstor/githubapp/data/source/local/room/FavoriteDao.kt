package com.itstor.githubapp.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.itstor.githubapp.data.source.local.entity.FavoriteEntity

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favoriteEntity: FavoriteEntity)

    @Update
    fun update(favoriteEntity: FavoriteEntity)

    @Delete
    fun delete(favoriteEntity: FavoriteEntity)

    @Query("SELECT * FROM favorite_user")
    fun getAll(): LiveData<List<FavoriteEntity>>

    @Query("SELECT * FROM favorite_user WHERE id = :id")
    fun getFavoriteById(id: Int): LiveData<FavoriteEntity>

    @Query("SELECT * FROM favorite_user WHERE login = :username")
    fun getFavoriteByUsername(username: String): LiveData<FavoriteEntity>

    @Query("DELETE FROM favorite_user WHERE id = :id")
    fun deleteById(id: Int)

    @Query("SELECT COUNT(id) FROM favorite_user WHERE id = :id")
    fun countById(id: Int): LiveData<Int>

    @Query("SELECT COUNT(login) FROM favorite_user WHERE login = :username")
    fun countByUsername(username: String): LiveData<Int>
}