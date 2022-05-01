package com.example.newsapi.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapi.R
import com.example.newsapi.adapters.ArticleListAdapter
import com.example.newsapi.ui.ArticleViewModel
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
                                                mViewModel.searchNews(editable.toString())
                                        }
                                }
                        }
                }

                mViewModel.searchNews.observe(viewLifecycleOwner) { response ->
                        when (response) {
                                is Resource.Success -> {
                                        hideProgressBar()
                                        response.data?.let { newsResponse ->
//                                                adapter.differ.submitList(newsResponse.articles)
                                                adapter.setData(newsResponse.articles)
                                                Log.d(TAG, "List is : ${newsResponse.articles}")
                                        }
                                }
                                is Resource.Error -> {
                                        hideProgressBar()
                                        response.message?.let { message ->
                                                Log.e(TAG, "An Error occurred: $message")
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
        }

        private fun hideProgressBar() {
                pbSearch.visibility = View.INVISIBLE
        }
}