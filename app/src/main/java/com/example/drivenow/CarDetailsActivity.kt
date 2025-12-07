// File: com/example/drivenow/CarDetailsActivity.kt
package com.example.drivenow

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CarDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_details)

        // Initialize Views
        val btnBack = findViewById<ImageView>(R.id.btn_back)
        val btnBookNow = findViewById<Button>(R.id.btn_book_now)
        val imgHero = findViewById<ImageView>(R.id.img_car_hero)
        val tvName = findViewById<android.widget.TextView>(R.id.tv_car_name)
        val tvTypeYear = findViewById<android.widget.TextView>(R.id.tv_car_type_year)
        val tvPrice = findViewById<android.widget.TextView>(R.id.tv_price)
        val tvSpecType = findViewById<android.widget.TextView>(R.id.tv_spec_type)
        val tvSpecSeats = findViewById<android.widget.TextView>(R.id.tv_spec_seats) // Added
        val tvSpecFuel = findViewById<android.widget.TextView>(R.id.tv_spec_fuel)   // Added

        // Populate Data from Intent
        val carName = intent.getStringExtra("CAR_NAME") ?: "Car Name"
        val carType = intent.getStringExtra("CAR_TYPE") ?: "Type"
        val carPrice = intent.getStringExtra("CAR_PRICE") ?: "$0"
        val carImage = intent.getStringExtra("CAR_IMAGE") ?: ""
        val carSeats = intent.getStringExtra("CAR_SEATS") ?: "Seats"
        val carFuel = intent.getStringExtra("CAR_FUEL") ?: "Fuel"

        tvName.text = carName
        tvTypeYear.text = carType
        tvPrice.text = carPrice
        tvSpecSeats.text = carSeats
        tvSpecFuel.text = carFuel
        
        // Extract just the body type if possible (e.g. from "2024 • Sedan" -> "Sedan")
        if (carType.contains("•")) {
             tvSpecType.text = carType.split("•")[1].trim()
        } else {
             tvSpecType.text = carType
        }

        // Load Image
        if (carImage.isNotEmpty()) {
            try {
                val imageBytes = android.util.Base64.decode(carImage, android.util.Base64.DEFAULT)
                val decodedImage = android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                imgHero.setImageBitmap(decodedImage)
            } catch (e: Exception) {
                e.printStackTrace()
                imgHero.setImageResource(R.drawable.image_with_fallback_mercedes_c_class) // Fallback
            }
        }

        btnBack.setOnClickListener {
            finish()
        }

        btnBookNow.setOnClickListener {
            Toast.makeText(this, "Proceeding to booking...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, BookCar::class.java)
            // Pass data to booking screen if needed
            intent.putExtra("CAR_NAME", carName)
            intent.putExtra("CAR_PRICE", carPrice)
            intent.putExtra("CAR_TYPE", carType)
            intent.putExtra("CAR_IMAGE", carImage)
            startActivity(intent)
        }
    }
}