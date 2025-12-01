package com.example.drivenow

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BookCar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_car)

        val proceedButton = findViewById<TextView>(R.id.text_confirm_booking)
        proceedButton.setOnClickListener {
            val intent = Intent(this, Bookings::class.java)
            startActivity(intent)
        }
    }
}