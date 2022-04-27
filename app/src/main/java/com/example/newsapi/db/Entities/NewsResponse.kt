package com.example.newsapi.db.Entities

import com.example.newsapi.db.Entities.Article

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)