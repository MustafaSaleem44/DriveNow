// File: com/example/drivenow/CarDetailsActivity.kt
package com.example.drivenow

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CarDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_details)

        // Initialize Views
        val btnBack = findViewById<ImageView>(R.id.btn_back)
        val btnBookNow = findViewById<Button>(R.id.btn_book_now)

        // Populate Data (In real app, get this from Intent extras)
        // val carName = intent.getStringExtra("CAR_NAME") ?: "Toyota Camry"
        // ...

        // For now, views are hardcoded with the "Toyota Camry" example
        // matching the XML, so we just handle interactions.

        btnBack.setOnClickListener {
            finish()
        }

        btnBookNow.setOnClickListener {
            Toast.makeText(this, "Proceeding to booking...", Toast.LENGTH_SHORT).show()
            // Next step: Open Booking Confirmation Screen
            // val intent = Intent(this, BookingProcessActivity::class.java)
            // startActivity(intent)
        }
    }
}