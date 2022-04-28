package com.example.newsapi.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapi.db.Entities.Article
import com.google.android.play.core.install.model.AppUpdateType

@Database(entities = [Article::class], version = 1)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {
        abstract fun getArticleDao(): ArticleDao

        companion object {
                @Volatile
                private var instance: ArticleDatabase? = null
                fun getDatabase(context: Context): ArticleDatabase {
                        if (instance == null) {
                                synchronized(this) {
                                        instance = Room.databaseBuilder(
                                                context.applicationContext,
                                                ArticleDatabase::class.java,
                                                "Article_Database"
                                        ).build()
                                }
                        }
                        return instance!!
                }

        }
}