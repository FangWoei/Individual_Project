package com.foo.individualproject.ui.add

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
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

class AddWordViewModel (
    private val repo: WordRepo
): ViewModel() {
    val title: MutableLiveData<String> = MutableLiveData("")
    val meaning: MutableLiveData<String> = MutableLiveData("")
    val synonyms: MutableLiveData<String> = MutableLiveData("")
    val details: MutableLiveData<String> = MutableLiveData("")
    val finish: MutableSharedFlow<Unit> = MutableSharedFlow()
    val snackbar: MutableLiveData<String> = MutableLiveData()

    fun submit() {
        if(title.value.isNullOrEmpty() || meaning.value.isNullOrEmpty() || synonyms.value.isNullOrEmpty() || details.value.isNullOrEmpty()) {
            snackbar.value = "Please fill in all fields"
            return
        }
        if(title.value != null && meaning.value != null && synonyms.value != null && details.value != null) {
            viewModelScope.launch(Dispatchers.IO) {
                repo.addWord(Word(title = title.value!!, meaning = meaning.value!!, synonyms = synonyms.value!!, details = details.value!!))
                withContext(Dispatchers.Main) {
                    snackbar.value = "Successfully Add"
                }
            }

            viewModelScope.launch {
                finish.emit(Unit)
            }
        }
        Log.d("add", "${title.value}, ${meaning.value}, ${synonyms.value}, ${details.value}")
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val repo = (this[APPLICATION_KEY] as WordApp).repo
                AddWordViewModel(repo)
            }
        }
    }

}