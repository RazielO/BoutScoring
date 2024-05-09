package com.razielo.boutscoring.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.razielo.boutscoring.data.models.Bout
import com.razielo.boutscoring.data.models.BoutWithFighters
import com.razielo.boutscoring.data.repository.BoutRepository
import kotlinx.coroutines.launch

class BoutViewModel(private val boutRepository: BoutRepository) : ViewModel() {
    val bouts: LiveData<List<BoutWithFighters>> = boutRepository.bouts.asLiveData()

    fun insert(bout: BoutWithFighters) = viewModelScope.launch {
        boutRepository.insert(bout)
    }

    fun update(bout: Bout) = viewModelScope.launch {
        boutRepository.update(bout)
    }

    fun delete(bout: BoutWithFighters) = viewModelScope.launch {
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