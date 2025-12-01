package com.example.drivenow

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CompanyDashboardOverview : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_dashboard_overview)

        val carsButton = findViewById<TextView>(R.id.text_cars)
        carsButton.setOnClickListener {
            val intent = Intent(this, CompanyDashboardCars::class.java)
            startActivity(intent)
        }

        val bookingsButton = findViewById<TextView>(R.id.text_bookings)
        bookingsButton.setOnClickListener {
            val intent = Intent(this, CompanyDashboardBookings::class.java)
            startActivity(intent)
        }
    }
}