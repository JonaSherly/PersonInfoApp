package com.example.sample2.mainEditor

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.lifecycle.ViewModelProviders
import com.example.sample2.*
import com.example.sample2.database.Database
import com.example.sample2.model.Person
import com.example.sample2.model.ValidationResult
import com.example.sample2.peopleTable.PeopleList
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

// To insert and update Person details
class MainActivity : AppCompatActivity() {

    private lateinit var nameText: EditText
    private lateinit var dobText: EditText
    private lateinit var emailText: EditText
    private lateinit var alertText: TextView
    private lateinit var viewModel: MainActivityViewModel
    private var isUpdating = false
    private var updatingPersonId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameText = findViewById(R.id.nameEditText)
        dobText = findViewById(R.id.dobEditText)
        emailText = findViewById(R.id.emailEditText)
        alertText = findViewById(R.id.alert)

        //Create viewModel factory instance
        val viewModelFactory = MainActivityViewModelFactory(
            MainRepository(Database.getInstance(this).taskDao)
        )

        //Get viewModel instance
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel::class.java)

        viewModel.notificationMessage.observe(this, androidx.lifecycle.Observer {
            Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
        })
        val cal = Calendar.getInstance()

        val dateSetListener = object : DatePickerDialog.OnDateSetListener { //Date Picker dialog listener is set
            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView(cal)
            }
        }

        dobText.keyListener = null


        date.setOnClickListener {

            val dateDialog = DatePickerDialog(
                this@MainActivity,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            dateDialog.datePicker.maxDate = Date().time
            dateDialog.show()

        }

        val submitButton = findViewById<Button>(R.id.submitButton)

        //Submit person details
        submitButton.setOnClickListener {
            submitData()
        }

        val nextButton = findViewById<ImageView>(R.id.button)

        //Go to list page
        nextButton.setOnClickListener {
            if (isUpdating) isUpdating = false
            val intent = Intent(this, PeopleList::class.java)
            startActivityForResult(intent, 1)

        }
    }
    // Display the selected data on the screen
    @SuppressLint("SimpleDateFormat")
    private fun updateDateInView(c: Calendar) {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat)
        dobText.setText(sdf.format(c.getTime())).toString()
    }

    // Insert new data or update existing data
    private fun submitData() {
        val name = nameText.text.toString()
        val dob = dobText.text.toString()
        val email = emailText.text.toString()
        val personData = Person(name, dob, email, updatingPersonId)
        val validationResult = viewModel.validate(personData)
        if (validationResult == ValidationResult.NoError) {
            alert.visibility = TextView.GONE

            if (!isUpdating) // insertion
                viewModel.insertPersonData(personData)
            else {
                viewModel.upDatePersonData(personData) // updation
                isUpdating = false
            }

            setData(Person("", "", "")) //clear text views
            val intent = Intent(this, PeopleList::class.java)
            startActivityForResult(intent, 1)
        } else {
            alert.visibility = TextView.VISIBLE
            alert.text = validationResult.text
        }


    }

    //Set values to edit text views
    private fun setData(data: Person) {
        nameText.setText(data.name).toString()
        dobText.setText(data.dob).toString()
        emailText.setText(data.emailID).toString()
    }



    //Get result from list page
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        setData(Person("", "", ""))
        if (requestCode == 1) {
            if (resultCode == 1) {
                val ppldata = data?.extras?.get("data") as Person //Get person data selected
                isUpdating = true //set updation condition to true
                updatingPersonId = ppldata.personId //set current personid to be to updated
                setData(ppldata)
            }
        }
    }
}
