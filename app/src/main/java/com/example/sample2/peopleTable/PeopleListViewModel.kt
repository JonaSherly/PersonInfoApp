package com.example.sample2.peopleTable

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sample2.model.Person
import com.example.sample2.database.ErrorData
import com.example.sample2.database.ResultData
import com.example.sample2.database.SuccessData
import kotlinx.coroutines.*

//List page viewmodel
class PeopleListViewModel(private val peopleRepository: PeopleListRepository): ViewModel(){

    private var viewModelJob = Job()
    private var _notificationMessage = MutableLiveData<String>()
    val notificationMessage:LiveData<String>
        get() = _notificationMessage
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCleared() {
        viewModelJob.cancel()
    }
    private var _peopleData = MutableLiveData<List<Person>>()
    val peopleData:LiveData<List<Person>>
    get() = _peopleData

    //Delete person data
    fun deletePersonData(personData: Person)
    {
        uiScope.launch {
            deleteData(personData)
            val resultData = getAllData()
            if (resultData is ErrorData)
            {
                _notificationMessage.value = resultData.errorMsg
                _peopleData.value = resultData.data
            }
            else if (resultData is SuccessData)
                _peopleData.value = resultData.data
        }
    }

    private suspend fun deleteData(personData: Person)
    {
        withContext(Dispatchers.IO)
        {
            peopleRepository.deletePersonData(personData)
        }
    }

    //Get all person data
    fun getAll()
    {
        uiScope.launch {
           val resultData = getAllData()
            if (resultData is ErrorData)
            {
                _notificationMessage.value = resultData.errorMsg
                _peopleData.value = resultData.data
            }
            else if (resultData is SuccessData)
                _peopleData.value = resultData.data

        }
    }
    private suspend fun getAllData():ResultData<List<Person>>
    {
       return withContext(Dispatchers.IO)
        {
            peopleRepository.getAll()
        }
    }
}