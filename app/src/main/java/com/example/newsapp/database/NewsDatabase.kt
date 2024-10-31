package com.example.newsapp.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.newsapp.model.NewsModel

@Database(entities = [NewsModel::class], version = 5, exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun NewsDao(): NewsDao?

    companion object {
        private val LOG_TAG = NewsDatabase::class.java.simpleName
        private val LOCK = Any()
        private const val DATABASE_NAME = "news"
        private var sInstance: NewsDatabase? = null
        fun getInstance(context: Context): NewsDatabase? {
            if (sInstance == null) {
                synchronized(LOCK) {
                    Log.d(LOG_TAG, "Creating new database instance")
                    sInstance = Room.databaseBuilder(
                        context.applicationContext,
                        NewsDatabase::class.java, DATABASE_NAME
                    ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
                }
            }
            Log.d(LOG_TAG, "Getting the database instance")
            return sInstance
        }
    }
}