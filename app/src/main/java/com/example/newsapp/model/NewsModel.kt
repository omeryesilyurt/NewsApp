package com.example.newsapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "dbNews",indices = [Index(value = ["key"], unique = true)])
data class NewsModel (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @SerializedName("key")
    @ColumnInfo(name = "key")
    val key: String?,

    @SerializedName("url")
    @ColumnInfo(name = "url")
    val url: String?,

    @SerializedName("description")
    @ColumnInfo(name = "description")
    val description: String?,

    @SerializedName("image")
    @ColumnInfo(name = "image")
    val image: String?,

    @SerializedName("name")
    @ColumnInfo(name = "name")
    val name: String?,

    @SerializedName("source")
    @ColumnInfo(name = "source")
    val source: String?,

    @ColumnInfo(name = "is_favorite")
    var isFavorite: Boolean? = false
): Serializable