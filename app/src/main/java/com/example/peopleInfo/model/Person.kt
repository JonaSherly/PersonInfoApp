package com.example.peopleInfo.model

import java.io.Serializable

//Data class to store Person details
data class Person(
    val name: String,
    val dob: String,
    val emailID: String,
    val personId: Int = 0
) : Serializable
