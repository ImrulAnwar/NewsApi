package com.example.newsapi.ui

import androidx.room.Entity
import androidx.room.PrimaryKey


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