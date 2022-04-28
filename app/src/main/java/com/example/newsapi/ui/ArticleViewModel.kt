package com.example.newsapi.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapi.db.ArticleDatabase
import com.example.newsapi.db.Entities.Article
import com.example.newsapi.db.Entities.NewsResponse
import com.example.newsapi.db.repositories.ArticleRepository
import com.example.newsapi.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response

class ArticleViewModel(application: Application) : AndroidViewModel(application) {
        private val repository: ArticleRepository = ArticleRepository(ArticleDatabase.getDatabase(application))
        init {
                getBreakingNews("us")
        }
        fun upsert(item: Article) {
                viewModelScope.launch(Dispatchers.IO) {
                        repository.upsert(item)
                }
        }

        fun delete(item: Article) {
                viewModelScope.launch(Dispatchers.IO) {
                        repository.delete(item)
                }
        }

        fun getAllArticles(): LiveData<List<Article>> {
                return repository.getAllArticles()
        }

        val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
        var breakingNewsPage = 1
        val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
        var searchNewsPage = 1

        fun getBreakingNews(countryCode: String){
                viewModelScope.launch(Dispatchers.IO) {
                        breakingNews.postValue(Resource.Loading())
                        val response = repository.getBreakingNews(countryCode, breakingNewsPage)
                        breakingNews.postValue(handleBreakingNewsResponse(response))
                }
        }

        fun searchNews(searchQuery: String) {
                viewModelScope.launch(Dispatchers.IO){
                        searchNews.postValue(Resource.Loading())
                        val response =repository.searchNews(searchQuery, searchNewsPage)
                        searchNews.postValue(handleSearchNewsResponse(response))
                }

        }


        private fun handleBreakingNewsResponse(response: Response<NewsResponse>) :Resource<NewsResponse>{
                if (response.isSuccessful) {
                        response.body()?.let {resultResponse->
                                return Resource.Success(resultResponse)
                        }
                }
                return Resource.Error(response.message())
        }
        private fun handleSearchNewsResponse(response: Response<NewsResponse>) :Resource<NewsResponse>{
                if (response.isSuccessful) {
                        response.body()?.let {resultResponse->
                                return Resource.Success(resultResponse)
                        }
                }
                return Resource.Error(response.message())
        }
}