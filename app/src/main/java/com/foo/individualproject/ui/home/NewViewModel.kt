package com.foo.individualproject.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.foo.individualproject.WordApp
import com.foo.individualproject.data.model.Word
import com.foo.individualproject.data.repository.WordRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow


class NewViewModel (
    private val repo: WordRepo
): ViewModel() {
    val sortTitle: MutableLiveData<String> = MutableLiveData("title")
    val sortOrder: MutableLiveData<String> = MutableLiveData("ASC")
    private val _query: MutableLiveData<String> = MutableLiveData("")
    val query: LiveData<String> = _query
    val finish: MutableSharedFlow<Unit> = MutableSharedFlow()

    fun setQuery(query: String) = _query.postValue(query)

    fun getAll(): Flow<List<Word>> = repo.getAllWords(query.value!!)

    fun getAllCompleted(): Flow<List<Word>> = repo.getAllCompletedWords(query.value!!)

    fun sortWord(words: List<Word>): List<Word> {
        return if (sortOrder.value == "ASC") {
            if(sortTitle.value == "title") words.sortedBy { it.title }
            else words.sortedBy { it.dateCreated }
        } else {
            if(sortTitle.value == "title") words.sortedByDescending { it.title }
            else words.sortedByDescending { it.dateCreated }
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                NewViewModel(
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as WordApp).repo
                )
            }
        }
    }
}