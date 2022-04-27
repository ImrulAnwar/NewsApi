package com.example.newsapi.api

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapi.R
import com.example.newsapi.db.Entities.Article
import kotlinx.android.synthetic.main.article_item.view.*

class ArticleListAdapter(var items: List<Article>) : RecyclerView.Adapter<ArticleListAdapter.ArticleViewHolder>() {
        inner class ArticleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
        //this is new
        // to update only the items that is changed instead of "notifyDatasetsChanged()" method

        private val differCallback = object : DiffUtil.ItemCallback<Article>() {
                override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                        return oldItem.url == newItem.url
                }

                override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                        return oldItem==newItem
                }
        }
        val differ = AsyncListDiffer(this, differCallback)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.article_item, parent, false)
                return ArticleViewHolder(view)
        }

        override fun getItemCount(): Int {
                return items.size
        }


        override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
                val currentArticle = differ.currentList[position]
                holder.itemView.apply {
                        Glide.with(this).load(currentArticle.urlToImage).into(ivArticleImage)
                        tvArticleTitle.text = currentArticle.title
                        tvArticleDescription.text = currentArticle.description
                        setOnClickListener{

                        }
                }
        }
}