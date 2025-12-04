package com.example.drivenow

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddCarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_car)

        // 1. Initialize Views
        val btnBack = findViewById<ImageView>(R.id.btn_back)
        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        val btnSubmit = findViewById<Button>(R.id.btn_add_car_submit)

        val spinnerType = findViewById<Spinner>(R.id.spinner_car_type)
        val spinnerSeats = findViewById<Spinner>(R.id.spinner_seats)
        val spinnerFuel = findViewById<Spinner>(R.id.spinner_fuel)

        val etName = findViewById<EditText>(R.id.et_car_name)
        val etYear = findViewById<EditText>(R.id.et_model_year)
        val etRate = findViewById<EditText>(R.id.et_daily_rate)

        // 2. Setup Spinners with Dummy Data
        setupSpinner(spinnerType, listOf("Select Type", "Sedan", "SUV", "Convertible", "Hatchback", "Electric"))
        setupSpinner(spinnerSeats, listOf("Select Seats", "2 Seats", "4 Seats", "5 Seats", "7 Seats"))
        setupSpinner(spinnerFuel, listOf("Select Fuel", "Gasoline", "Diesel", "Hybrid", "Electric"))

        // 3. Button Listeners
        btnBack.setOnClickListener { finish() }
        btnCancel.setOnClickListener { finish() }

        btnSubmit.setOnClickListener {
            if (validateInputs(etName, etYear, etRate, spinnerType)) {
                // Success logic
                Toast.makeText(this, "Car Added Successfully!", Toast.LENGTH_SHORT).show()
                finish() // Go back to Dashboard
            }
        }
    }

    private fun setupSpinner(spinner: Spinner, items: List<String>) {
        // Create an adapter for the spinner
        val adapter = object : ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items) {
            // Custom view for the selected item (closed state)
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent) as TextView
                view.setTextColor(Color.WHITE) // Make text white
                return view
            }

            // Custom view for the dropdown list (open state)
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(Color.BLACK) // Make dropdown text black so it's readable on default white bg
                return view
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun validateInputs(name: EditText, year: EditText, rate: EditText, type: Spinner): Boolean {
        if (name.text.isEmpty()) {
            name.error = "Required"
            return false
        }
        if (year.text.isEmpty()) {
            year.error = "Required"
            return false
        }
        if (rate.text.isEmpty()) {
            rate.error = "Required"
            return false
        }
        if (type.selectedItemPosition == 0) {
            Toast.makeText(this, "Please select a car type", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}