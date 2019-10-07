package com.example.sample2.peopleTable

import com.example.sample2.model.Person
import com.example.sample2.database.PersonDao
import com.example.sample2.database.ResultData

//Repository to handle list page operations
class PeopleListRepository(private val personDao: PersonDao) {

    //Get person details from database
    fun getAll():ResultData<List<Person>>
    {
        return personDao.getAll()
    }
    //Delete person entry from database
    fun deletePersonData(personData: Person):ResultData<Boolean>
    {
        return personDao.delete(personData)
    }
}

