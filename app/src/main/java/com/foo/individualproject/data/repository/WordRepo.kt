package com.foo.individualproject.data.repository

import com.foo.individualproject.data.db.WordDao
import com.foo.individualproject.data.model.Word
import kotlinx.coroutines.flow.Flow

class WordRepo (
    private val dao: WordDao
    ) {
    fun getAllWords(query: String): Flow<List<Word>> {
        return dao.getAll(query)
    }

    fun getAllCompletedWords(query: String): Flow<List<Word>> {
        return dao.getAllCompleted(query)
    }

    fun getWordById(id: Int): Word? {
        return dao.getWordsById(id)
    }

    fun addWord(words: Word) {
        dao.add(words)
    }

    fun updateWord(words: Word) {
        dao.update(words)
    }

    fun deleteWord(words: Word) {
        dao.delete(words)
    }

    }