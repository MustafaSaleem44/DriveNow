package com.example.drivenow

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CompanyDashboardBookings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_dashboard_bookings)

        val overviewButton = findViewById<TextView>(R.id.text_overview)
        overviewButton.setOnClickListener {
            val intent = Intent(this, CompanyDashboardOverview::class.java)
            startActivity(intent)
        }

        val carsButton = findViewById<TextView>(R.id.text_cars)
        carsButton.setOnClickListener {
            val intent = Intent(this, CompanyDashboardCars::class.java)
            startActivity(intent)
        }
    }
}