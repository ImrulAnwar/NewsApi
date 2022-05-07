package com.example.newsapi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapi.adapters.ArticleListAdapter
import com.example.newsapi.ui.ArticleViewModel
import com.example.newsapi.util.Constants.Companion.QUERY_PAGE_SIZE
import com.example.newsapi.util.Resource
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.android.synthetic.main.fragment_category_list.*
import kotlinx.android.synthetic.main.fragment_category_list.view.*


class CategoryListFragment : Fragment() {
        private lateinit var mViewModel: ArticleViewModel
        private lateinit var adapter: ArticleListAdapter
        private val TAG = "CategoryListFragment"
        var isLoading = false
        var isLastPage = false
        var isScrolling = false
        private val args by navArgs<CategoryListFragmentArgs>()

        override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
        ): View? {
                // Inflate the layout for this fragment
                val view = inflater.inflate(R.layout.fragment_category_list, container, false)
                view.tvCategoryList.text = args.category
                val recyclerView = view.rvCategories
                mViewModel = ViewModelProvider(this)[ArticleViewModel::class.java]
                adapter = ArticleListAdapter(TAG)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                mViewModel.getCategorizedNews(args.categoryCode).observe(viewLifecycleOwner){response->
                        when (response) {
                                is Resource.Success -> {
                                        hideProgressBar()
                                        response.data?.let {newsResponse ->
                                                adapter.setData(newsResponse.articles.toList())
                                                val totalPages = newsResponse.totalResults/QUERY_PAGE_SIZE+2
                                                isLastPage = mViewModel.categorizedNewsPage == totalPages
                                        }
                                }
                                is Resource.Error ->{
                                        hideProgressBar()
                                        response.message?.let {message->
                                                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                                        }
                                }
                                is Resource.Loading->{
                                        showProgressBar()
                                }
                        }
                }
                return view
        }
        private fun hideProgressBar() {
                pbCategories.visibility = View.INVISIBLE
                isLoading = false
        }

        private fun showProgressBar() {
                pbCategories.visibility = View.VISIBLE
                isLoading = true
        }
}