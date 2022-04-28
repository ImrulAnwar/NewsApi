package com.example.newsapi.db.repositories

import androidx.lifecycle.LiveData
import com.example.newsapi.api.RetrofitInstance
import com.example.newsapi.db.ArticleDatabase
import com.example.newsapi.db.Entities.Article
import com.example.newsapi.db.Entities.NewsResponse
import retrofit2.Response

class ArticleRepository(private val db: ArticleDatabase) {
        //from room
        fun getAllArticles(): LiveData<List<Article>> {
                return db.getArticleDao().getAllArticles()
        }

        suspend fun upsert(item: Article) {
                db.getArticleDao().upsert(item)
        }

        suspend fun delete(item: Article) {
                db.getArticleDao().delete(item)
        }

        //from api
        suspend fun getBreakingNews(countryCode: String, pageNumber: Int): Response<NewsResponse> {
                return RetrofitInstance.api.getBreakingNews(
                        countryCode = countryCode,
                        pageNumber = pageNumber
                )
        }

        suspend fun searchNews(searchQuery: String, pageNumber: Int): Response<NewsResponse> {
                return RetrofitInstance.api.searchForNews(
                        searchQuery = searchQuery,
                        pageNumber = pageNumber
                )
        }
}