package com.example.newsapi.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapi.R
import com.example.newsapi.adapters.ArticleListAdapter
import com.example.newsapi.ui.ArticleViewModel
import com.example.newsapi.util.Constants
import com.example.newsapi.util.Constants.Companion.QUERY_PAGE_SIZE
import com.example.newsapi.util.Constants.Companion.SEARCH_DELAY
import com.example.newsapi.util.Resource
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.android.synthetic.main.fragment_search_news.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment() {
        private lateinit var mViewModel: ArticleViewModel
        private lateinit var adapter: ArticleListAdapter
        private val TAG = "SearchNewsFragment"
        var isLoading = false
        var isLastPage = false
        var isScrolling = false


        override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
        ): View? {
                // Inflate the layout for this fragment
                val view = inflater.inflate(R.layout.fragment_search_news, container, false)
                val recyclerView = view.rvSearch
                adapter = ArticleListAdapter(TAG)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                mViewModel = ViewModelProvider(this)[ArticleViewModel::class.java]
                var job: Job? = null
                view.evSearch.addTextChangedListener { editable ->
                        job?.cancel()
                        job = MainScope().launch {
                                delay(SEARCH_DELAY)
                                editable?.let {
                                        if (editable.toString().isNotEmpty()) {
                                                mViewModel.getSearchNews(editable.toString())
                                        }
                                }
                        }
                }
                recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrollStateChanged(
                                recyclerView: RecyclerView,
                                newState: Int
                        ) {
                                super.onScrollStateChanged(recyclerView, newState)
                                isScrolling = true
                        }

                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                                super.onScrolled(recyclerView, dx, dy)
                                if (mViewModel.shouldPaginate(
                                                recyclerView,
                                                isLoading = isLoading,
                                                isScrolling = isScrolling,
                                                isLastPage = isLastPage
                                        )
                                ) {
                                        mViewModel.getSearchNews(evSearch.text.toString())
                                        isScrolling = false
                                }
                        }
                })

                mViewModel.searchNews.observe(viewLifecycleOwner) { response ->
                        when (response) {
                                is Resource.Success -> {
                                        hideProgressBar()
                                        response.data?.let { newsResponse ->
//                                                adapter.differ.submitList(newsResponse.articles)
                                                adapter.setData(newsResponse.articles.toList())
                                                val totalPages =
                                                        newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                                                isLastPage =
                                                        mViewModel.searchNewsPage == totalPages
                                        }
                                }
                                is Resource.Error -> {
                                        hideProgressBar()
                                        response.message?.let { message ->
                                                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                                        }
                                }
                                is Resource.Loading -> {
                                        showProgressBar()
                                }
                        }
                }
                return view
        }

        private fun showProgressBar() {
                pbSearch.visibility = View.VISIBLE
                isLoading = true
        }

        private fun hideProgressBar() {
                pbSearch.visibility = View.INVISIBLE
                isLoading = false
        }
}