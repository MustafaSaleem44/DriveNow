package com.example.drivenow

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.drivenow.data.AppDatabase
import com.example.drivenow.data.Car
import com.example.drivenow.data.CarStatus
import com.example.drivenow.repository.CarRepository
import com.example.drivenow.utils.SharedPreferencesHelper
import kotlinx.coroutines.launch

class AddCar : AppCompatActivity() {
    private lateinit var carNameEditText: EditText
    private lateinit var modelYearEditText: EditText
    private lateinit var carTypeSpinner: Spinner
    private lateinit var seatingCapacitySpinner: Spinner
    private lateinit var fuelTypeSpinner: Spinner
    private lateinit var dailyRateEditText: EditText
    private lateinit var imageUrlEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var pickupLocationEditText: EditText
    private lateinit var addButton: Button
    private lateinit var cancelButton: Button
    
    private lateinit var carRepository: CarRepository
    private lateinit var prefsHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_car)

        val database = AppDatabase.getDatabase(this)
        carRepository = CarRepository(database.carDao(), this)
        prefsHelper = SharedPreferencesHelper(this)

        initializeViews()
        setupSpinners()
        setupClickListeners()
    }

    private fun initializeViews() {
        carNameEditText = findViewById(R.id.edit_car_name)
        modelYearEditText = findViewById(R.id.edit_model_year)
        carTypeSpinner = findViewById(R.id.spinner_car_type)
        seatingCapacitySpinner = findViewById(R.id.spinner_seating_capacity)
        fuelTypeSpinner = findViewById(R.id.spinner_fuel_type)
        dailyRateEditText = findViewById(R.id.edit_daily_rate)
        imageUrlEditText = findViewById(R.id.edit_image_url)
        descriptionEditText = findViewById(R.id.edit_description)
        pickupLocationEditText = findViewById(R.id.edit_pickup_location)
        addButton = findViewById(R.id.btn_text_add_fleet)
        cancelButton = findViewById(R.id.btn_text_cancel)

        findViewById<View>(R.id.button_back).setOnClickListener {
            finish()
        }
    }

    private fun setupSpinners() {

        val carTypes = arrayOf("Sedan", "SUV", "Convertible", "Electric")
        carTypeSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, carTypes)
            .apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        // Seating Capacity
        val capacities = (2..8).map { it.toString() }.toTypedArray()
        seatingCapacitySpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, capacities)
            .apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        // Fuel Type
        val fuelTypes = arrayOf("Gasoline", "Hybrid", "Electric", "Diesel")
        fuelTypeSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, fuelTypes)
            .apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
    }

    private fun setupClickListeners() {
        addButton.setOnClickListener {
            createCar()
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun createCar() {
        val companyId = prefsHelper.getUserId()
        if (companyId == null) {
            Toast.makeText(this, "Please login as a company", Toast.LENGTH_SHORT).show()
            return
        }

        val carName = carNameEditText.text.toString().trim()
        val modelYearStr = modelYearEditText.text.toString().trim()
        val dailyRateStr = dailyRateEditText.text.toString().trim()


        if (carName.isEmpty()) {
            carNameEditText.error = "Car name is required"
            return
        }

        if (modelYearStr.isEmpty()) {
            modelYear.EditText.error="Model year is unfilled, it is required to succesfully go forward"
            modelYearEditText.error = "Model year is required"
            return
        }

        val modelYear = modelYearStr.toIntOrNull()
        if (modelYear == null || modelYear < 1900 || modelYear > 2030) {
            modelYearEditText.error = "Invalid model year"
            return
        }

        if (dailyRateStr.isEmpty()) {
            dailyRateEditText.error = "Daily rate is required"
            return
        }

        val dailyRate = dailyRateStr.toDoubleOrNull()
        if (dailyRate == null || dailyRate <= 0) {
            dailyRateEditText.error = "Invalid daily rate"
            return
        }

        val description = descriptionEditText.text.toString().trim()
        if (description.isEmpty()) {
            descriptionEditText.error = "Description is required"
            return
        }

        lifecycleScope.launch {
            val car = Car(
                id = "car_${System.currentTimeMillis()}",
                companyId = companyId,
                carName = carName,
                modelYear = modelYear,
                carType = carTypeSpinner.selectedItem.toString(),
                seatingCapacity = seatingCapacitySpinner.selectedItem.toString().toInt(),
                fuelType = fuelTypeSpinner.selectedItem.toString(),
                dailyRate = dailyRate,
                imageUrl = imageUrlEditText.text.toString().trim().takeIf { it.isNotEmpty() },
                description = description,
                features = null,
                pickupLocation = pickupLocationEditText.text.toString().trim().takeIf { it.isNotEmpty() },
                status = CarStatus.AVAILABLE
            )

            carRepository.createCar(car).onSuccess {
                Toast.makeText(this@AddCar, "Car added successfully", Toast.LENGTH_SHORT).show()
                finish()
            }.onFailure { exception ->
                Toast.makeText(
                    this@AddCar,
                    exception.message ?: "Failed to add car",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}