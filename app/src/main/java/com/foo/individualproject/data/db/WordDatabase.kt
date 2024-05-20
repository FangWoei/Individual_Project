package com.foo.individualproject.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.foo.individualproject.core.Convertor
import com.foo.individualproject.data.model.Word

@Database(entities = [Word::class], version = 2)
@TypeConverters(Convertor::class)
abstract class WordDatabase: RoomDatabase() {
    abstract fun wordDao(): WordDao

    companion object {
        const val NAME = "my_words"
    }
}