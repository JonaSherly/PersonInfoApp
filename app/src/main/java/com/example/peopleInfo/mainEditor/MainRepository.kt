package com.example.peopleInfo.mainEditor

import com.example.peopleInfo.model.Person
import com.example.peopleInfo.database.PersonDao
import com.example.peopleInfo.database.ResultData

//Repository to handle main editor page operations
class MainRepository(private val personDao: PersonDao) {
    //Insert person data into database
    fun insert(personData: Person): ResultData<Boolean> {
        return personDao.insert(personData)
    }

    //Update person details in database
    fun updatePersonData(personData: Person): ResultData<Boolean> {
        return personDao.update(personData)
    }
}