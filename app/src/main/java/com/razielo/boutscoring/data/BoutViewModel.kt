package com.razielo.boutscoring.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.razielo.boutscoring.data.models.BoutInfo
import com.razielo.boutscoring.data.repository.BoutRepository
import com.razielo.boutscoring.ui.models.ParsedBout
import kotlinx.coroutines.launch

class BoutViewModel(private val boutRepository: BoutRepository) : ViewModel() {
    val bouts: LiveData<List<ParsedBout>> = boutRepository.bouts.asLiveData()
    val bout = MutableLiveData<ParsedBout>()

    private val _filtered = MutableLiveData<List<ParsedBout>>()
    val filtered: LiveData<List<ParsedBout>>
        get() = _filtered

    fun getAllFighterBouts(id: String) = viewModelScope.launch {
        _filtered.value = boutRepository.getAllFighterBouts(id)
    }

    fun searchAllFighterBouts(query: String) = viewModelScope.launch {
        _filtered.value = boutRepository.searchAllFighterBouts("%$query%")
    }

    fun insert(bout: ParsedBout) = viewModelScope.launch {
        boutRepository.insert(bout)
    }

    fun update(bout: ParsedBout) = viewModelScope.launch {
        boutRepository.update(bout)
    }

    fun updateInfo(info: BoutInfo) = viewModelScope.launch {
        boutRepository.updateInfo(info)
    }

    fun delete(bout: ParsedBout) = viewModelScope.launch {
        boutRepository.deleteBout(bout)
    }
}

class BoutViewModelFactory(private val boutRepository: BoutRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BoutViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BoutViewModel(boutRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}