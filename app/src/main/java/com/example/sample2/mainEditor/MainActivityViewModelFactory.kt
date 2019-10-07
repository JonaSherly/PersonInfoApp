package com.example.sample2.mainEditor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

//MainActivity viewModel factory to pass repository instance as a parameter
class MainActivityViewModelFactory(private val mainRepository: MainRepository):
    ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            return MainActivityViewModel(mainRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}