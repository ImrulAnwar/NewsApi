package com.example.newsapi.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapi.db.ArticleDatabase
import com.example.newsapi.db.Entities.Article
import com.example.newsapi.db.Entities.NewsResponse
import com.example.newsapi.db.repositories.ArticleRepository
import com.example.newsapi.util.Constants
import com.example.newsapi.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class ArticleViewModel(application: Application) : AndroidViewModel(application) {
        private val articleRepository: ArticleRepository =
                ArticleRepository(ArticleDatabase.getDatabase(application))

        fun upsertArticle(item: Article) {
                viewModelScope.launch(Dispatchers.IO) {
                        articleRepository.upsertArticle(item)
                }
        }

        fun deleteArticle(item: Article) {
                viewModelScope.launch(Dispatchers.IO) {
                        articleRepository.deleteArticle(item)
                }
        }

        fun getSavedArticles(): LiveData<List<Article>> {
                return articleRepository.getSavedArticles()
        }

        private val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
        var breakingNewsPage = 1
        val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
        var searchNewsPage = 1
        private var breakingNewsResponse: NewsResponse? = null
        var searchNewsResponse: NewsResponse? = null


        fun getBreakingNews(countryCode: String = "us"): LiveData<Resource<NewsResponse>> {
                viewModelScope.launch(Dispatchers.IO) {
                        breakingNews.postValue(Resource.Loading())
                        val response =
                                articleRepository.getBreakingNews(countryCode, breakingNewsPage)
                        breakingNews.postValue(handleBreakingNewsResponse(response))
                }
                return breakingNews
        }

        fun getSearchNews(searchQuery: String) {
                viewModelScope.launch(Dispatchers.IO) {
                        searchNews.postValue(Resource.Loading())
                        val response = articleRepository.searchNews(searchQuery, searchNewsPage)
                        searchNews.postValue(handleSearchNewsResponse(response))
                }
        }


        private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
                if (response.isSuccessful) {
                        response.body()?.let { resultResponse ->
                                breakingNewsPage++
                                if (breakingNewsResponse == null) {
                                        breakingNewsResponse = resultResponse
                                } else {
                                        val oldArticles = breakingNewsResponse?.articles
                                        val newArticles = resultResponse.articles
                                        oldArticles?.addAll(newArticles)
                                }
                                return Resource.Success(breakingNewsResponse ?: resultResponse)
                        }
                }
                return Resource.Error(response.message())
        }

        private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
                if (response.isSuccessful) {
                        response.body()?.let { resultResponse ->
                                searchNewsPage++
                                if (searchNewsResponse == null) {
                                        searchNewsResponse = resultResponse
                                } else {
                                        val oldArticles = searchNewsResponse?.articles
                                        val newArticles = resultResponse.articles
                                        oldArticles?.addAll(newArticles)
                                }
                                return Resource.Success(searchNewsResponse ?: resultResponse)
                        }
                }
                return Resource.Error(response.message())
        }

        fun shouldPaginate(
                recyclerView: RecyclerView,
                isLoading: Boolean,
                isLastPage: Boolean,
                isScrolling: Boolean
        ): Boolean {
                val layoutManager =
                        recyclerView.layoutManager as LinearLayoutManager
                val firstVisibleItemPosition =
                        layoutManager.findFirstVisibleItemPosition()
                val visibleItemCount = layoutManager.childCount
                val totalCount = layoutManager.itemCount
                val isNotLoadingAndNotAtLastPage = !isLoading && !isLastPage
                val isAtLastItem =
                        firstVisibleItemPosition + visibleItemCount >= totalCount
                val isNotAtBeginning = firstVisibleItemPosition >= 0
                val isTotalMoreThanVisible = totalCount >= Constants.QUERY_PAGE_SIZE
                return isNotLoadingAndNotAtLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
        }
}