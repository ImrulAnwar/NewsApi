package com.example.newsapi.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.newsapi.R
import com.example.newsapi.db.Entities.Article
import com.example.newsapi.ui.ArticleViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_article.view.*

class ArticleFragment : Fragment() {
        private lateinit var mViewModel: ArticleViewModel
        val args by navArgs<ArticleFragmentArgs>()
        private lateinit var article: Article
        override fun onCreateView(
                inflater: LayoutInflater,
                container: ViewGroup?,
                savedInstanceState: Bundle?
        ): View? {
                val view = inflater.inflate(R.layout.fragment_article, container, false)
                view.fabSaveArticle.setOnClickListener {
                        mViewModel.upsertArticle(article)
                        Snackbar.make(view, " Article Saved", Snackbar.LENGTH_SHORT).show()
                }
                return view
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)
                mViewModel  = ViewModelProvider(this)[ArticleViewModel::class.java]
                article = args.article
                view.wvArticle.apply {
                        webViewClient = WebViewClient()
                        loadUrl(article.url!!)
                }
        }
}