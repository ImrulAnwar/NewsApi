package com.example.newsapi.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapi.R
import com.example.newsapi.db.Entities.Article
import kotlinx.android.synthetic.main.article_item.view.*

class ArticleListAdapter(val from: String) :
        RecyclerView.Adapter<ArticleListAdapter.ArticleViewHolder>() {
        private var items = emptyList<Article>()

        inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        //this is new
        // to update only the items that is changed instead of "notifyDatasetsChanged()" method
//        private val differCallback = object : DiffUtil.ItemCallback<Article>() {
//                override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
//                        return oldItem.url == newItem.url
//                }
//
//                override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
//                        return oldItem==newItem
//                }
//        }
//        val differ = AsyncListDiffer(this, differCallback)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.article_item, parent, false)
                return ArticleViewHolder(view)
        }

        override fun getItemCount(): Int {
                return items.size
        }

        fun setData(list: List<Article>) {
                items = list
                notifyDataSetChanged()
        }


        override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
//                val currentArticle = differ.currentList[position]
                val currentArticle = items[position]
                holder.itemView.apply {
                        Glide.with(this).load(currentArticle.urlToImage).into(ivArticleImage)
                        tvArticleTitle.text = currentArticle.title
                        tvArticleDescription.text = currentArticle.description
                        tvSource.text = currentArticle.source.name
                        setOnClickListener {
                                val bundle = Bundle().apply {
                                        putSerializable("article", currentArticle)
                                }
                                if (from == "BreakingNewsFragment")
                                        findNavController().navigate(R.id.action_breakingNewsFragment_to_articleFragment, bundle)
                                else if (from == "SearchNewsFragment")
                                        findNavController().navigate(R.id.action_searchNewsFragment_to_articleFragment, bundle)
                                else if (from == "SavedNewsFragment")
                                        findNavController().navigate(R.id.action_savedNewsFragment_to_articleFragment, bundle)
                        }
                }
        }
}