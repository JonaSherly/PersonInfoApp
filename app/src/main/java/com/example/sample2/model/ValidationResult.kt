package com.example.sample2.model

//Validation result Enum
enum class ValidationResult(val errorCode:Int,val text:String) {

    EmptySpacesError(1,"No empty spaces allowed"),
    EmailFormatError(2,"Enter valid email"),
    DateFormatError(3,"Enter valid date"),
    NameFormatError(4,"Enter valid name"),
    NoError(0,"")

}