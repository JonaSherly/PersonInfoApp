package com.example.sample2.mainEditor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sample2.model.Person
import com.example.sample2.model.ValidationResult
import com.example.sample2.database.ErrorData
import com.example.sample2.database.ResultData
import kotlinx.coroutines.*

//Main editor page viewmodel
class MainActivityViewModel(private val mainRepository: MainRepository) :ViewModel()
{
    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _notificationMessage = MutableLiveData<String>()
    val notificationMessage:LiveData<String>
    get() = _notificationMessage

    override fun onCleared(){
        viewModelJob.cancel()
    }

    //Validate entered data
    fun validate(personData: Person): ValidationResult {
        val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        val NAME_REGEX = "[a-zA-Z][a-zA-Z ]+[a-zA-Z]$"
        var validationResult = ValidationResult.NoError
        val name = personData.name
        val email = personData.emailID
        val dob = personData.dob
        if (email == "" || name == "" || dob == "") validationResult =
            ValidationResult.EmptySpacesError
        else if(  !EMAIL_REGEX.toRegex().matches(email))
            validationResult = ValidationResult.EmailFormatError
        else if (!NAME_REGEX.toRegex().matches(name))
            validationResult = ValidationResult.NameFormatError
        return validationResult

    }

    //Insert into Database
    fun insertPersonData(personData: Person)
    {
        //Launch coroutine
        uiScope.launch {
           val resultData = insertData(personData)
            if(resultData is ErrorData) //Notify incase of error
                _notificationMessage.value = resultData.errorMsg
        }
    }

    //Suspend function to run insert operation in IO thread
    private suspend fun insertData(personData: Person):ResultData<Boolean>
    {
       return withContext(Dispatchers.IO)
        {
            mainRepository.insert(personData)

        }
    }

    //Update data in Database
    fun upDatePersonData(personData: Person)
    {
        //Launch coroutine
        uiScope.launch {
           val resultData = updateData(personData)
            if(resultData is ErrorData) //Notify incase of error
                _notificationMessage.value = resultData.errorMsg
        }
    }

    //Suspend function to run update operation in IO thread
    private suspend fun updateData(personData: Person):ResultData<Boolean>
    {
        return withContext(Dispatchers.IO)
        {
            mainRepository.updatePersonData(personData)
        }
    }

}