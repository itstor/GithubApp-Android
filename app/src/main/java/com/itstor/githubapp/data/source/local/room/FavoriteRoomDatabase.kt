package com.itstor.githubapp.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.itstor.githubapp.data.source.local.entity.FavoriteEntity

@Database(entities = [FavoriteEntity::class], version = 1, exportSchema = false)
abstract class FavoriteRoomDatabase : RoomDatabase() {
    abstract fun favoriteUserDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): FavoriteRoomDatabase {
            if (INSTANCE == null) {
                synchronized(FavoriteRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        FavoriteRoomDatabase::class.java, "favorite_user_database")
                        .build()
                }
            }
            return INSTANCE as FavoriteRoomDatabase
        }
    }
}