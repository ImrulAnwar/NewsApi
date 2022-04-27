package com.example.newsapi.db.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newsapi.db.Entities.Source


@Entity(
    tableName = "Article_Table"
)
data class Article(
    @PrimaryKey(autoGenerate = true)
    val id: Int?=null,
    val author: String,
    val content: Any,
    val description: String,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
)