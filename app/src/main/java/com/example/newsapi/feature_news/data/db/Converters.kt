package com.example.newsapi.feature_news.data.db

import androidx.room.TypeConverter
import com.example.newsapi.feature_news.data.entities.Source

class Converters {
        @TypeConverter
        fun fromSource(source: Source): String {
                return source.name!!
        }

        @TypeConverter
        fun toSource(name: String): Source {
                return Source("0", name)
        }
}