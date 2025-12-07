package com.example.drivenow

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.drivenow.data.AppDatabase
import com.example.drivenow.data.Car
import com.example.drivenow.repository.CarRepository
import kotlinx.coroutines.launch

class CarDetails : AppCompatActivity() {
    private lateinit var carImage: ImageView
    private lateinit var carName: TextView
    private lateinit var carDetails: TextView
    private lateinit var priceText: TextView
    private lateinit var statusBadge: TextView
    private lateinit var seatsText: TextView
    private lateinit var fuelTypeText: TextView
    private lateinit var carTypeText: TextView
    private lateinit var descriptionText: TextView
    private lateinit var featuresText: TextView
    private lateinit var pickupLocationText: TextView
    private lateinit var bookButton: View
    
    private lateinit var carRepository: CarRepository
    private var carId: String? = null
    private var currentCar: Car? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_details)

        carId = intent.getStringExtra("carId")
        if (carId == null) {
            finish()
            return
        }

        val database = AppDatabase.getDatabase(this)
        carRepository = CarRepository(database.carDao(), this)

        initializeViews()
        setupClickListeners()
        loadCarDetails()
    }

    private fun initializeViews() {
        carImage = findViewById(R.id.image_car)
        carName = findViewById(R.id.text_car_name)
        carDetails = findViewById(R.id.text_car_details)
        priceText = findViewById(R.id.text_price)
        statusBadge = findViewById(R.id.text_status)
        seatsText = findViewById(R.id.text_seats)
        fuelTypeText = findViewById(R.id.text_fuel_type)
        carTypeText = findViewById(R.id.text_car_type)
        descriptionText = findViewById(R.id.text_description)
        featuresText = findViewById(R.id.text_features)
        pickupLocationText = findViewById(R.id.text_pickup_location)
        bookButton = findViewById(R.id.button_book_now)

        findViewById<View>(R.id.button_back).setOnClickListener {
            finish()
        }
    }

    private fun setupClickListeners() {
        bookButton.setOnClickListener {
            currentCar?.let { car ->
                val intent = Intent(this, BookCar::class.java).apply {
                    putExtra("carId", car.id)
                }
                startActivity(intent)
            }
        }
    }

    private fun loadCarDetails() {
        carId?.let { id ->
            lifecycleScope.launch {
                carRepository.getCarByIdFlow(id).collect { car ->
                    car?.let {
                        currentCar = it
                        displayCarDetails(it)
                    } ?: run {
                        // Car not found, try refreshing from server
                        carRepository.refreshCarsFromServer()
                    }
                }
            }
        }
    }

    private fun displayCarDetails(car: Car) {
        carName.text = car.carName
        carDetails.text = "${car.modelYear} • ${car.carType}"
        priceText.text = "$${car.dailyRate.toInt()}"
        
        seatsText.text = "${car.seatingCapacity} Seats"
        fuelTypeText.text = car.fuelType
        carTypeText.text = car.carType
        
        descriptionText.text = car.description ?: "No description available"
        
        // Features
        car.features?.let {
            featuresText.text = it.replace(",", "\n• ")
                .let { str -> if (str.isNotEmpty()) "• $str" else "No features listed" }
        } ?: run {
            featuresText.text = "No features listed"
        }
        
        pickupLocationText.text = car.pickupLocation ?: "Location to be determined"

        // Load image
        car.imageUrl?.let { imageUrl ->
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.image_fallback)
                .error(R.drawable.image_fallback)
                .into(carImage)
        } ?: carImage.setImageResource(R.drawable.image_fallback)

        // Status badge
        statusBadge.text = car.status.name
        statusBadge.visibility = if (car.status == com.example.drivenow.data.CarStatus.AVAILABLE) {
            View.VISIBLE
        } else {
            View.GONE
        }

        // Enable/disable book button based on availability
        bookButton.isEnabled = car.status == com.example.drivenow.data.CarStatus.AVAILABLE
    }
}