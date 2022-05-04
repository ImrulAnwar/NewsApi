package com.example.newsapi.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.newsapi.R
import com.example.newsapi.db.Entities.Article
import com.example.newsapi.ui.ArticleViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_article.view.*

class ArticleFragment : Fragment() {
        private lateinit var mViewModel: ArticleViewModel
        val args by navArgs<ArticleFragmentArgs>()
        private lateinit var article: Article

        @SuppressLint("SetJavaScriptEnabled")
        override fun onCreateView(
                inflater: LayoutInflater,
                container: ViewGroup?,
                savedInstanceState: Bundle?
        ): View? {

                val view = inflater.inflate(R.layout.fragment_article, container, false)
                mViewModel  = ViewModelProvider(this)[ArticleViewModel::class.java]
                article = args.article

                view.wvArticle.apply {
                        webViewClient = WebViewClient()
                        loadUrl(article.url)
                        // to fit the webView to the screen
                        settings.loadWithOverviewMode = true
                        settings.useWideViewPort = true
                        settings.javaScriptEnabled = true
                }
                view.fabSaveArticle.setOnClickListener {
                        mViewModel.upsertArticle(article)
                        Snackbar.make(view, " Article Saved", Snackbar.LENGTH_SHORT).show()
                }
                // to handle on back pressed
//                activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
//                        override fun handleOnBackPressed() {
//                                if (view.wvArticle.canGoBack()) {
//                                        view.wvArticle.goBack()
//                                } else {
//                                        activity!!.onBackPressed()
//                                }
//                        }
//                })
                return view
        }

}