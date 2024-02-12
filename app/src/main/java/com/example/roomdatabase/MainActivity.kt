package com.example.roomdatabase

import android.health.connect.datatypes.units.Length
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.roomdb.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appDb: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appDb = AppDatabase.getDatabase(this)

        binding.btnWriteData.setOnClickListener {

            writeData()
        }

        binding.btnReadData.setOnClickListener {
            println("TAG Button Clicked")
            readData()
        }

        binding.btnUpdate.setOnClickListener {

            updateData()
        }

        binding.btnDeleteAll.setOnClickListener {

            GlobalScope.launch {
                appDb.studentDao().deleteAll()
            }
        }

    }

    private fun writeData() {

        var firstName = binding.etFirstName.text.toString()
        var lastName = binding.etLastName.text.toString()
        var rollNo = binding.etRollNo.text.toString()

        if (firstName.isNotEmpty() && lastName.isNotEmpty() && rollNo.isNotEmpty()) {

            var student = Student(
                null, firstName, lastName, rollNo.toInt()
            )
            GlobalScope.launch(Dispatchers.IO)
            {
                appDb.studentDao().insert(student)

            }

            binding.etFirstName.text.clear()
            binding.etLastName.text.clear()
            binding.etRollNo.text.clear()

            Toast.makeText(this, "Successfully Written", Toast.LENGTH_SHORT).show()
        } else {

            Toast.makeText(this, "Please Enter Date", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun displayData(student: Student) {

        withContext(Dispatchers.Main) {

            println("TAG student first name" + student.firstName)

            // to set the data to textview from the table
            binding.tvFirstName.text = student.firstName
            binding.tvLastName.text = student.lastName
            binding.tvRollNo.text = student.rollNo.toString()

            // to change visibility of textviews
            binding.tvFirstName.visibility = View.VISIBLE
            binding.tvLastName.visibility = View.VISIBLE
            binding.tvRollNo.visibility = View.VISIBLE

        }
    }

    private fun readData() {

        println("TAG Read Data")

        var rollNo = binding.etRollNoRead.text.toString()

        println("TAG Roll No: " + rollNo)

        if (rollNo.isNotEmpty()) {

            println("TAG Roll No not empty ")

            lateinit var student : Student

            GlobalScope.launch(Dispatchers.IO) {

                student = appDb.studentDao().findByRoll(rollNo.toInt())
                println("TAG Student Detail: " + student.toString())
                displayData(student)
            }


        }


    }

    private fun updateData() {

        var firstName = binding.etFirstName.text.toString()
        var lastName = binding.etLastName.text.toString()
        var rollNo = binding.etRollNo.text.toString()

        if (firstName.isNotEmpty() && lastName.isNotEmpty() && rollNo.isNotEmpty()) {
            GlobalScope.launch(Dispatchers.IO)
            {
                appDb.studentDao().updateData(firstName, lastName, rollNo.toInt())
            }

            binding.etFirstName.text.clear()
            binding.etLastName.text.clear()
            binding.etRollNo.text.clear()

            Toast.makeText(this, "Successfully Updated", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "All Fields Need to be Filled", Toast.LENGTH_SHORT).show()

        }
    }


}