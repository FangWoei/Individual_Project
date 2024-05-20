package com.foo.individualproject.ui.update

import android.util.Log
import android.widget.Toast
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
import kotlinx.coroutines.withContext

class UpdateWordViewModel(
    private val repo: WordRepo
): ViewModel() {
    private val _word: MutableLiveData<Word> = MutableLiveData()
    val word: LiveData<Word> = _word
    val title: MutableLiveData<String> = MutableLiveData("")
    val meaning: MutableLiveData<String> = MutableLiveData("")
    val synonyms: MutableLiveData<String> = MutableLiveData("")
    val details: MutableLiveData<String> = MutableLiveData("")
    val edit: MutableSharedFlow<Unit> = MutableSharedFlow()
    val snackbar: MutableLiveData<String> = MutableLiveData()

    fun getWordById(id: Int) {
        viewModelScope.launch (Dispatchers.IO){
            _word.postValue(repo.getWordById(id))
        }
    }

    fun setWord() {
        word.value?.let {
            title.value = it.title
            meaning.value = it.meaning
            synonyms.value = it.synonyms
            details.value = it.details
        }

    }

    fun edit() {
        if(title.value.isNullOrEmpty() || meaning.value.isNullOrEmpty() || synonyms.value.isNullOrEmpty() || details.value.isNullOrEmpty()) {
            snackbar.value = "Please fill in all fields"
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val word = word.value
            if (word != null) {
                repo.updateWord(
                    word.copy(
                        title = title.value!!,
                        meaning = meaning.value!!,
                        synonyms = synonyms.value!!,
                        details = details.value!!
                    )
                )
                withContext(Dispatchers.Main) {
                    snackbar.value = "Successfully Updated"
                }
            }
            edit.emit(Unit)
        }
        Log.d("update", "${title.value}, ${meaning.value}, ${synonyms.value}, ${details.value}")
    }


    companion object {
        val Factory = viewModelFactory {
            initializer {
                val repo = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as WordApp).repo
                UpdateWordViewModel(repo)
            }
        }
    }
}
