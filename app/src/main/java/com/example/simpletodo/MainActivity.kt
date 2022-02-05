package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate( savedInstanceState: Bundle? ) {
        super.onCreate( savedInstanceState )
        setContentView( R.layout.activity_main )

        val onLongClickListener = object: TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked( position: Int ) {
                // remove item from list
                listOfTasks.removeAt( position )

                // notify adapter of data change
                adapter.notifyDataSetChanged()

                saveItems()
            }
        }

        // Let's detect when user clicks on the 'Add' button
//        findViewById<Button>( R.id.button ).setOnClickListener {
//            // Executed when user clicks a button
//            Log.i( "Matt", "clicked on a button" )
//        }

        // populate the task list from cache file
        loadItems()

        // Look up recyclerView in the layout
        val recyclerView = findViewById<RecyclerView>( R.id.recyclerView )

        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter( listOfTasks, onLongClickListener )

        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter

        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager( this )

        // set up button and input field so user can enter a task and add it to the list

        val inputTextField = findViewById<EditText>( R.id.addTaskField )

        // get reference to a button and attach onClick listener
        findViewById<Button>( R.id.button ).setOnClickListener {
            // - get text user has input into @id/addTaskField
            val userInputTask = inputTextField.text.toString()

            // - add string to list of tasks: listOfTasks
            listOfTasks.add( userInputTask )

            // Notify adapter data has been updated
            adapter.notifyItemInserted( listOfTasks.size - 1 )

            // - Reset text field
            inputTextField.setText("")

            saveItems()
        }
    }

    // save data input by user
    // save data by reading/writing a file


    // get data file
    fun getDataFile() : File {

        // every line in file represents a specific item in list of tasks
        return File( filesDir, "data.txt" )
    }

    // load the items by reading every line in file
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines( getDataFile(), Charset.defaultCharset() )
        } catch( ioException: IOException ) {
            ioException.printStackTrace()
        }
    }


    // save all items - write to data file
    fun saveItems() {
        try {
            FileUtils.writeLines( getDataFile(), listOfTasks )
        } catch(ioException: IOException ) {
            ioException.printStackTrace()
        }
    }
}