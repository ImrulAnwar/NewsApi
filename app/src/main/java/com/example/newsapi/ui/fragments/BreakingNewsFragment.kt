package com.example.newsapi.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapi.R
import com.example.newsapi.adapters.ArticleListAdapter
import com.example.newsapi.ui.ArticleViewModel
import com.example.newsapi.ui.activities.MainActivity
import com.example.newsapi.util.Resource
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.android.synthetic.main.fragment_breaking_news.view.*

class BreakingNewsFragment : Fragment() {
        private lateinit var mViewModel: ArticleViewModel
        private lateinit var adapter: ArticleListAdapter
        private val TAG = "BreakingNewsFragment"

        override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
        ): View? {
                val view = inflater.inflate(R.layout.fragment_breaking_news, container, false)
                val recyclerView = view.rvBreakingNews
                adapter = ArticleListAdapter()
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(requireContext())

                mViewModel = ViewModelProvider(this)[ArticleViewModel::class.java]
                mViewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
                        when (response) {
                                is Resource.Success -> {
                                        hideProgressBar()
                                        response.data?.let { newsResponse ->
//                                                adapter.differ.submitList(newsResponse.articles)
                                                adapter.setData(newsResponse.articles)
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

                })

                return view
        }

        private fun hideProgressBar() {
                pbPagination.visibility = View.INVISIBLE
        }

        private fun showProgressBar() {
                pbPagination.visibility = View.VISIBLE
        }
}