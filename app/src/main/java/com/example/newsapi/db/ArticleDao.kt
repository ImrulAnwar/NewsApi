package com.example.newsapi.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsapi.db.Entities.Article

@Dao
interface ArticleDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun upsert(article: Article): Long

        @Query("SELECT * FROM Article_Table")
        fun getAllArticles(): LiveData<List<Article>>

        @Delete
        suspend fun delete(article: Article)
}