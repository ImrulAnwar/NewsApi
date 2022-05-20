package com.example.newsapi.feature_news.ui.activity_main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapi.R
import com.example.newsapi.adapters.ArticleListAdapter
import com.example.newsapi.feature_news.ui.ArticleViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_saved_news.view.*

class SavedNewsFragment : Fragment() {
        private lateinit var mViewModel: ArticleViewModel
        private lateinit var adapter: ArticleListAdapter
        private val TAG = "SavedNewsFragment"
        override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
        ): View? {
                // Inflate the layout for this fragment
                val view = inflater.inflate(R.layout.fragment_saved_news, container, false)
                mViewModel = ViewModelProvider(this)[ArticleViewModel::class.java]
                adapter = ArticleListAdapter(TAG)
                val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
                        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                ) {
                        override fun onMove(
                                recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder
                        ): Boolean {
                                return true
                        }

                        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                                val position = viewHolder.adapterPosition
                                val article = adapter.items[position]
                                mViewModel.deleteArticle(article)
                                Snackbar.make(view, "Article Deleted!", Snackbar.LENGTH_LONG).apply {
                                        setAction("Undo"){
                                                mViewModel.upsertArticle(article)
                                        }
                                        show()
                                }
                        }
                }

                ItemTouchHelper(itemTouchHelperCallback).apply {
                        attachToRecyclerView(view.rvSavedNews)
                }
                mViewModel.getSavedArticles().observe(viewLifecycleOwner) { articles ->
                        adapter.setData(articles)
                }
                val recyclerView = view.rvSavedNews
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                return view
        }


}