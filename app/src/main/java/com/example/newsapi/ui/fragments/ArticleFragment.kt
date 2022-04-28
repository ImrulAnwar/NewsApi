package com.example.newsapi.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.newsapi.R
import com.example.newsapi.ui.ArticleViewModel

class ArticleFragment : Fragment() {
        private lateinit var mViewModel: ArticleViewModel
        override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
        ): View? {
                // Inflate the layout for this fragment
                val view = inflater.inflate(R.layout.fragment_article, container, false)
                mViewModel  = ViewModelProvider(this)[ArticleViewModel::class.java]

                return view
        }
}