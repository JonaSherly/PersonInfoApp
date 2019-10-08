package com.example.peopleInfo.peopleTable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

//ListPage viewModel factory to pass repository instance as a parameter
class PeopleListViewModelFactory(private val repository: PeopleListRepository) :
    ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PeopleListViewModel::class.java)) {
            return PeopleListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}