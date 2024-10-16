package com.example.newsapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.model.NewsModel

@Dao
interface NewsDao {

    @Query("SELECT * FROM dbNews")
    fun getAllFavoriteNews(): List<NewsModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFavorite(favNews: NewsModel)

    @Query("DELETE FROM dbNews WHERE id = :newsId")
    fun removeFavorite(newsId: Long)

}