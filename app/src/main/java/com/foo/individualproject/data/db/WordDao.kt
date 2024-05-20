package com.foo.individualproject.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.foo.individualproject.data.model.Word
import kotlinx.coroutines.flow.Flow


@Dao
interface WordDao {
    @Query("SELECT * FROM Word WHERE isCompleted = 0 AND title LIKE '%' || :query || '%' ORDER BY title ASC")
    fun getAll(query: String): Flow<List<Word>>

    @Query("SELECT * FROM Word WHERE isCompleted = 1 AND title LIKE '%' || :query || '%' ORDER BY title ASC")
    fun getAllCompleted(query: String): Flow<List<Word>>

    @Query("SELECT * FROM Word WHERE id = :id")
    fun getWordsById(id: Int): Word?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(word: Word)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(word: Word)

    @Delete
    fun delete(word: Word)
}