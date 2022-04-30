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
import com.example.newsapi.ui.ArticleViewModel
import kotlinx.android.synthetic.main.fragment_article.*

class ArticleFragment : Fragment() {
        private lateinit var mViewModel: ArticleViewModel
        val args by navArgs<ArticleFragmentArgs>()
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

                val view = inflater.inflate(R.layout.fragment_article, container, false)
                mViewModel  = ViewModelProvider(this)[ArticleViewModel::class.java]

                return view
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)
                val article = args.article
                wvArticle.apply {
                        webViewClient = WebViewClient()
                        loadUrl(article.url)
                }

        }
}