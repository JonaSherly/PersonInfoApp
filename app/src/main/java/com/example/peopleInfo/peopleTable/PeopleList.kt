package com.example.peopleInfo.peopleTable

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.peopleInfo.R
import com.example.peopleInfo.database.Database

//Class to display the list of entries
class PeopleList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_people_list)

        //Create viewModel factory instance
        val viewModelFactory = PeopleListViewModelFactory(
            PeopleListRepository(Database.getInstance(this).taskDao)
        )
        //Get viewModel instance
        val viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(PeopleListViewModel::class.java)
        val list = findViewById<RecyclerView>(R.id.peopleList)
        val listAdapter =
            PeopleListAdapter(OnItemClickListener { peopleData ->
                //Go to main editor page to update data on selection
                val intent = Intent()
                intent.putExtra("data", peopleData)
                setResult(1, intent)
                finish()

            }, viewModel)
        list.adapter = listAdapter

        //Call getAll function for the initial update of list
        viewModel.getAll()

        //Observe peopleData list to update list on change
        viewModel.peopleData.observe(this, Observer { peopleList ->
            listAdapter.submitList(peopleList)//Submit list to list adapter

        })
        // display toast
        viewModel.notificationMessage.observe(this, Observer {
            Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
        })

        //Enable back button in the action bar to go to home page(main editor page)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //Up(back) Button is enabled
        supportActionBar?.setDisplayShowHomeEnabled(true) //Display up Button

    }

    //Go back to main editor page to insert new data
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) this.finish()
        return true
    }
}
