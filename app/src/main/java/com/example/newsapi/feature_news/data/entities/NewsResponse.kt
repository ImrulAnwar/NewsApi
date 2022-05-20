package com.example.newsapi.feature_news.data.entities

data class NewsResponse(
        val articles: MutableList<Article>,
        val status: String,
        val totalResults: Int
)