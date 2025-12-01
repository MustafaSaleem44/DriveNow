package com.example.drivenow

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CompanyDashboardCars : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_dashboard_cars)

        val addNewCarButton = findViewById<TextView>(R.id.text_add_car)
        addNewCarButton.setOnClickListener {
            val intent = Intent(this, AddCar::class.java)
            startActivity(intent)
        }

        val overviewButton = findViewById<TextView>(R.id.text_overview)
        overviewButton.setOnClickListener {
            val intent = Intent(this, CompanyDashboardOverview::class.java)
            startActivity(intent)
        }

        val bookingsButton = findViewById<TextView>(R.id.text_bookings)
        bookingsButton.setOnClickListener {
            val intent = Intent(this, CompanyDashboardBookings::class.java)
            startActivity(intent)
        }
    }
}