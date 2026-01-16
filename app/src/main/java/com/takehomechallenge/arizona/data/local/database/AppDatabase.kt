package com.takehomechallenge.arizona.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.takehomechallenge.arizona.data.local.dao.CharacterDao
import com.takehomechallenge.arizona.data.local.dao.FavoriteDao
import com.takehomechallenge.arizona.data.local.dao.SearchHistoryDao
import com.takehomechallenge.arizona.data.local.entity.CharacterEntity
import com.takehomechallenge.arizona.data.local.entity.FavoriteEntity
import com.takehomechallenge.arizona.data.local.entity.RemoteKeysEntity
import com.takehomechallenge.arizona.data.local.entity.SearchHistoryEntity

@Database(
    entities = [
        CharacterEntity::class,
        FavoriteEntity::class,
        RemoteKeysEntity::class,
        SearchHistoryEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(DatabaseConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun searchHistoryDao(): SearchHistoryDao
}