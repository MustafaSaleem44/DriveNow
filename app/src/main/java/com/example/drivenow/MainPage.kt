package com.example.drivenow

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        val carDetailsButton = findViewById<ImageView>(R.id.image_fallback)
        carDetailsButton.setOnClickListener {
            val intent = Intent(this, CarDetails::class.java)
            startActivity(intent)
        }

        val bookingsButton = findViewById<ImageView>(R.id.image_icon1)
        bookingsButton.setOnClickListener {
            val intent = Intent(this, Bookings::class.java)
            startActivity(intent)
        }

        val profileButton = findViewById<ImageView>(R.id.image_icon2)
        profileButton.setOnClickListener {
            val intent = Intent(this, ProfilePage::class.java)
            startActivity(intent)
        }
    }
}