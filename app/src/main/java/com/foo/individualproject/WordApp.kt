package com.foo.individualproject

import android.app.Application
import androidx.room.Room
import com.foo.individualproject.data.db.WordDatabase
import com.foo.individualproject.data.repository.WordRepo

class WordApp: Application() {
    lateinit var repo: WordRepo

    override fun onCreate() {
        super.onCreate()
        val db = Room.databaseBuilder(
            this,
            WordDatabase::class.java,
            WordDatabase.NAME
        ).fallbackToDestructiveMigration().build()
        repo = WordRepo(db.wordDao())
    }
}