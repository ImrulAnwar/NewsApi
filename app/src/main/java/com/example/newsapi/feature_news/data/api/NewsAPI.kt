package com.example.newsapi.feature_news.data.api

import com.example.newsapi.feature_news.data.entities.NewsResponse
import com.example.newsapi.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {
        //Dao Equivalent
        @GET("v2/top-headlines")
        suspend fun getBreakingNews(
                @Query("country")
                countryCode: String = "us",
                @Query("page")
                pageNumber : Int = 1,
                @Query("apiKey")
                apiKey: String = API_KEY
        ): Response<NewsResponse>

        @GET("v2/everything")
        suspend fun getSearchNews(
                @Query("q")
                searchQuery: String,
                @Query("page")
                pageNumber : Int = 1,
                @Query("apiKey")
                apiKey: String = API_KEY
        ): Response<NewsResponse>

        @GET("v2/top-headlines")
        suspend fun getCategorizedNews(
                @Query("category")
                category: String,
                @Query("country")
                countryCode: String = "us",
                @Query("page")
                pageNumber: Int = 1,
                @Query("apiKey")
                apiKey: String = API_KEY
        ): Response<NewsResponse>
}