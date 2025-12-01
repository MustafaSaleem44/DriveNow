package com.example.drivenow

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CarDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_details)

        val bookNowButton = findViewById<TextView>(R.id.text_book_now)
        bookNowButton.setOnClickListener {
            val intent = Intent(this, BookCar::class.java)
            startActivity(intent)
        }
    }
}