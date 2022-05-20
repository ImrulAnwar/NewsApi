package com.example.newsapi.feature_news.data.repo

import androidx.lifecycle.LiveData
import com.example.newsapi.feature_news.data.api.RetrofitInstance
import com.example.newsapi.feature_news.data.db.ArticleDatabase
import com.example.newsapi.feature_news.data.entities.Article
import com.example.newsapi.feature_news.data.entities.NewsResponse
import retrofit2.Response

class ArticleRepository(private val db: ArticleDatabase) {
        //from room
        fun getSavedArticles(): LiveData<List<Article>> {
                return db.getArticleDao().getSavedArticles()
        }

        suspend fun upsertArticle(item: Article) {
                db.getArticleDao().upsertArticle(item)
        }

        suspend fun deleteArticle(item: Article) {
                db.getArticleDao().deleteArticle(item)
        }

        //from api
        suspend fun getBreakingNews(countryCode: String, pageNumber: Int): Response<NewsResponse> {
                return RetrofitInstance.api.getBreakingNews(
                        countryCode = countryCode,
                        pageNumber = pageNumber
                )
        }

        suspend fun getSearchNews(searchQuery: String, pageNumber: Int): Response<NewsResponse> {
                return RetrofitInstance.api.getSearchNews(
                        searchQuery = searchQuery,
                        pageNumber = pageNumber
                )
        }

        suspend fun getCategorizedNews(category: String): Response<NewsResponse> {
                return RetrofitInstance.api.getCategorizedNews(category = category)
        }
}