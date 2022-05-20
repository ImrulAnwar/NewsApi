package com.example.newsapi.feature_news.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsapi.feature_news.data.entities.Article

@Dao
interface ArticleDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun upsertArticle(article: Article): Long

        @Query("SELECT * FROM Article_Table")
        fun getSavedArticles(): LiveData<List<Article>>

        @Delete
        suspend fun deleteArticle(article: Article)
}