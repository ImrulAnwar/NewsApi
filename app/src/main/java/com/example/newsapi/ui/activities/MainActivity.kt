package com.example.newsapi.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsapi.R
import com.example.newsapi.db.Entities.Article
import com.example.newsapi.ui.ArticleViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
        private lateinit var mViewModel: ArticleViewModel
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_main)

                //nav host fragment does not have a controller set up
                val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
                val navController = navHostFragment.navController
                bottomNavigationView.setupWithNavController(navController)

//                val appBarConfiguration = AppBarConfiguration(
//                        setOf(
//                                R.id.savedNewsFragment,
//                                R.id.searchNewsFragment,
//                                R.id.breakingNewsFragment
//                        )
//                )
//                setupActionBarWithNavController(navController, appBarConfiguration)

                mViewModel = ViewModelProvider(this)[ArticleViewModel::class.java]
        }
}