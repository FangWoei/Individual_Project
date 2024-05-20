package com.foo.individualproject.ui.selected

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.foo.individualproject.WordApp
import com.foo.individualproject.data.model.Word
import com.foo.individualproject.data.repository.WordRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class SelectedViewModel(
    private val repo: WordRepo
) : ViewModel() {
    private val _word: MutableLiveData<Word> = MutableLiveData()
    val word: LiveData<Word> = _word
    val title: MutableLiveData<String> = MutableLiveData()
    val meaning: MutableLiveData<String> = MutableLiveData()
    val synonyms: MutableLiveData<String> = MutableLiveData()
    val details: MutableLiveData<String> = MutableLiveData()
    val finish: MutableSharedFlow<Unit> = MutableSharedFlow()

    fun getWordById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _word.postValue(repo.getWordById(id))
        }
    }

    fun setWords() {
        word.value?.let {
            title.value = it.title
            meaning.value = it.meaning
            synonyms.value = it.synonyms
            details.value = it.details
        }

    }

    fun completeWords() {
        viewModelScope.launch(Dispatchers.IO) {
            val completeWord = word.value?.copy(isCompleted = true)
            repo.updateWord(completeWord!!)
            finish.emit(Unit)
        }

    }

    fun deleteWords() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteWord(word.value!!)
            finish.emit(Unit)
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val repo =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as WordApp).repo
                SelectedViewModel(repo)
            }
        }
    }
}