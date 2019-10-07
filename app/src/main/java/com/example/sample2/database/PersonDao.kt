package com.example.sample2.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.sample2.model.Person
import java.lang.Exception

//Person DAO class to get DB connection and perform DB operations
class PersonDao (context: Context):SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION){

    companion object{
        private val DATABASE_NAME = "PeopleDataBa"
        private val DATABASE_VERSION = 1
        private val TABLE_PERSON = "personTable"
        private val KEY_ID = "personId"
        private val KEY_NAME = "name"
        private val KEY_EMAIL = "email"
        private val KEY_DOB = "dob"
    }

    //Create Person table on PersonDao class creation
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_PEOPLEDATA_TABLE = ("CREATE TABLE $TABLE_PERSON ( $KEY_ID  INTEGER PRIMARY KEY AUTOINCREMENT, $KEY_NAME  TEXT, $KEY_EMAIL  TEXT, $KEY_DOB  TEXT )")
        db?.execSQL(CREATE_PEOPLEDATA_TABLE)

    }
    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS person_data")
        onCreate(db)
    }


    //Insertion
    fun insert(person: Person):ResultData<Boolean>{
        var resultData:ResultData<Boolean> = ErrorData(false,"insertion unsuccessful")
            val db = this.writableDatabase
        try{
            val contentValues = ContentValues()
            contentValues.put(KEY_NAME, person.name)
            contentValues.put(KEY_EMAIL, person.emailID )
            contentValues.put(KEY_DOB, person.dob)
            val success = db.insert(TABLE_PERSON, null, contentValues)
            if(success > -1) resultData = SuccessData(true) // resultData updated
        }
        catch (e:Exception)
        {

        }
        finally {
            db.close()
        }


        return resultData
    }

    //Get all person details
    fun getAll():ResultData<List<Person>>{
        var resultData:ResultData<List<Person>> = ErrorData(listOf(),"Data empty")
        var cursor: Cursor? = null
            val db = this.readableDatabase
        try {
            val selectQuery = "SELECT * FROM $TABLE_PERSON"
            cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                val people = mutableListOf<Person>()
                do {
                    val personId = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                    val name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                    val email = cursor.getString(cursor.getColumnIndex(KEY_EMAIL))
                    val dob = cursor.getString(cursor.getColumnIndex(KEY_DOB))
                    val person = Person(name, dob, email, personId)
                    people.add(person)
                } while (cursor.moveToNext())
                resultData = SuccessData(people) // resultData updated

            }
        }
        catch (e:Exception)
        {

        }
        finally {
            db.close()
            cursor?.close()
        }


        return resultData
    }

    //Deletion
    fun delete(person: Person):ResultData<Boolean>{

        var resultData:ResultData<Boolean> =  ErrorData(false, "deletion unsuccessful")
            val db = this.writableDatabase
        try {
            val success = db.delete(TABLE_PERSON, "$KEY_ID="+ person.personId , null)
            if (success > -1)
                resultData = SuccessData(true) // resultData updated
        }
        catch (e:Exception)
        {

        }
        finally {
            db.close()
        }

        return resultData
    }

    //Updation
    fun update(person: Person):ResultData<Boolean>{

        var resultData:ResultData<Boolean> = ErrorData(false, "updation unsuccessful")
            val db = this.writableDatabase
        try {
            val contentValues = ContentValues()
            contentValues.put(KEY_ID, person.personId)
            contentValues.put(KEY_NAME, person.name)
            contentValues.put(KEY_EMAIL, person.emailID)
            contentValues.put(KEY_DOB, person.dob)
            val success =
                db.update(TABLE_PERSON, contentValues, "$KEY_ID=" + person.personId, null)
            if (success > -1) resultData = SuccessData(true) // resultData updated
        }
        catch (e:Exception)
        {
            //resultData not updated  so returns ErrorData
        }
        finally {
            db.close()
        }

        return resultData
    }

}

//Class to return database operation results
sealed class ResultData<T>
data class SuccessData<T>(val data:T):ResultData<T>()
data class ErrorData<T>(val data:T, val errorMsg:String):ResultData<T>()

