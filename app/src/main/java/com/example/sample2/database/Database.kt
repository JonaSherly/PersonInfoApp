package com.example.sample2.database

import android.content.Context

//Database Singleton class to get DAO instance
class Database(context: Context) {

    val taskDao = PersonDao(context)

    companion object {
        @Volatile
        private var instance: Database? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: Database(context).also { instance = it }
        }
    }
}