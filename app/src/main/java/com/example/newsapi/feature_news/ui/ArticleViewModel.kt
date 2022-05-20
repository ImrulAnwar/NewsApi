package com.example.newsapi.feature_news.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapi.feature_news.data.db.ArticleDatabase
import com.example.newsapi.feature_news.data.entities.Article
import com.example.newsapi.feature_news.data.entities.NewsResponse
import com.example.newsapi.feature_news.data.repo.ArticleRepository
import com.example.newsapi.util.Constants
import com.example.newsapi.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class ArticleViewModel(application: Application) : AndroidViewModel(application) {
        private val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
        val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
        val categorizedNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
        var breakingNewsPage = 1
        var searchNewsPage = 1
        var categorizedNewsPage = 1
        private var breakingNewsResponse: NewsResponse? = null
        var searchNewsResponse: NewsResponse? = null
        var categorizedNewsResponse: NewsResponse? =null

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


        fun getBreakingNews(countryCode: String = "us"): LiveData<Resource<NewsResponse>> {
                viewModelScope.launch(Dispatchers.IO) {
                        safeBreakingNewsCall(countryCode)
                }
                return breakingNews
        }

        fun getSearchNews(searchQuery: String): MutableLiveData<Resource<NewsResponse>> {
                viewModelScope.launch(Dispatchers.IO) {
                        safeSearchNewsCall(searchQuery)
                }
                return searchNews
        }

        fun getCategorizedNews(category: String): MutableLiveData<Resource<NewsResponse>> {
                viewModelScope.launch(Dispatchers.IO){
                        safeCategorizedNewsCall(category)
                }
                return categorizedNews
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

        private fun hasInternetConnection(): Boolean {
                val connectivityManager =
                        getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        val activeNetwork = connectivityManager.activeNetwork ?: return false
                        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
                                ?: return false
                        return when {
                                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                                else -> false
                        }
                } else {
                        connectivityManager.activeNetworkInfo?.run {
                                when (type) {
                                        ConnectivityManager.TYPE_WIFI -> true
                                        ConnectivityManager.TYPE_MOBILE -> true
                                        ConnectivityManager.TYPE_ETHERNET -> true
                                        else -> false
                                }
                        }
                }
                return false
        }

        private suspend fun safeBreakingNewsCall(countryCode: String) {
                breakingNews.postValue(Resource.Loading())
                try {
                        if (hasInternetConnection()) {
                                val response = articleRepository.getBreakingNews(countryCode, breakingNewsPage)
                                breakingNews.postValue(handleBreakingNewsResponse(response))
                        } else {
                                breakingNews.postValue(Resource.Error("No Internet Connection"))
                        }
                } catch (t: Throwable) {
                        when (t) {
                                is IOException -> breakingNews.postValue(Resource.Error("Network Failure"))
                                else -> breakingNews.postValue(Resource.Error("Conversion Error"))
                        }
                }
        }

        private suspend fun safeSearchNewsCall(searchQuery: String) {
                // change searchNews, searchNewsPage, searchQuery
                searchNews.postValue(Resource.Loading())
                try {
                        if (hasInternetConnection()) {
                                val response =
                                        articleRepository.getSearchNews(
                                                searchQuery,
                                                searchNewsPage
                                        )
                                searchNews.postValue(handleSearchNewsResponse(response))
                        } else {
                                searchNews.postValue(Resource.Error("No Internet Connection"))
                        }
                } catch (t: Throwable) {
                        when (t) {
                                is IOException -> searchNews.postValue(Resource.Error("Network Failure"))
                                else -> searchNews.postValue(Resource.Error("Conversion Error"))
                        }
                }
        }
        private suspend fun safeCategorizedNewsCall(category: String) {
                categorizedNews.postValue(Resource.Loading())
                try {
                        if (hasInternetConnection()) {
                                val response =
                                        articleRepository.getCategorizedNews(category = category)
                                categorizedNews.postValue(handleCategorizedNewsResponse(response))
                        } else {
                                categorizedNews.postValue(Resource.Error("No Internet Connection"))
                        }
                }catch (t: Throwable) {
                        when (t) {
                                is IOException -> categorizedNews.postValue(Resource.Error("Network Failure"))
                                else -> categorizedNews.postValue(Resource.Error("Conversion Error"))
                        }
                }
        }

        private fun handleCategorizedNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
                if (response.isSuccessful) {
                        response.body()?.let { resultResponse->
                                categorizedNewsPage++
                                if (categorizedNewsResponse == null) {
                                        categorizedNewsResponse = resultResponse
                                } else {
                                        val oldArticles  = categorizedNewsResponse?.articles
                                        val newArticles = resultResponse.articles
                                        oldArticles?.addAll(newArticles)
                                }
                                return  Resource.Success(categorizedNewsResponse?:resultResponse)
                        }
                }
                return Resource.Error(response.message())
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
}