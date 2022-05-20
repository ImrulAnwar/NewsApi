package com.example.newsapi.feature_news.ui.activity_main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.newsapi.R
import kotlinx.android.synthetic.main.fragment_categories.view.*

class CategoriesFragment : Fragment() {
        override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
        ): View? {
                // Inflate the layout for this fragment
                val view = inflater.inflate(R.layout.fragment_categories, container, false)
                view.cvSports.setOnClickListener {
                        Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show()
                        val bundle = Bundle().apply {
                                putString("category", "Sports")
                                putString("categoryCode", "sports")
                        }
                        findNavController().navigate(R.id.action_categoriesFragment_to_categoryListFragment, bundle)
                }
                view.cvTechnology.setOnClickListener {
                        val bundle = Bundle().apply {
                                putString("category", "Technology")
                                putString("categoryCode", "technology")
                        }
                        findNavController().navigate(R.id.action_categoriesFragment_to_categoryListFragment, bundle)
                }
                view.cvScience.setOnClickListener {
                        val bundle = Bundle().apply {
                                putString("category", "Science")
                                putString("categoryCode", "science")
                        }
                        findNavController().navigate(R.id.action_categoriesFragment_to_categoryListFragment, bundle)
                }
                view.cvHealth.setOnClickListener {
                        val bundle = Bundle().apply {
                                putString("category", "Health")
                                putString("categoryCode", "health")
                        }
                        findNavController().navigate(R.id.action_categoriesFragment_to_categoryListFragment, bundle)
                }
                view.cvBusiness.setOnClickListener {
                        val bundle = Bundle().apply {
                                putString("category", "Business")
                                putString("categoryCode", "business")
                        }
                        findNavController().navigate(R.id.action_categoriesFragment_to_categoryListFragment, bundle)
                }
                view.cvEntertainment.setOnClickListener {
                        val bundle = Bundle().apply {
                                putString("category", "Entertainment")
                                putString("categoryCode", "entertainment")
                        }
                        findNavController().navigate(R.id.action_categoriesFragment_to_categoryListFragment, bundle)
                }
                return view
        }
}