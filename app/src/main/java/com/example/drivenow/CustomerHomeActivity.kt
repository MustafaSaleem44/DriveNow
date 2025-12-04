// File: com/example/drivenow/CustomerHomeActivity.kt
package com.example.drivenow

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CustomerHomeActivity : AppCompatActivity() {

    private lateinit var rvCars: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_home)

        rvCars = findViewById(R.id.rv_customer_cars)

        // Dummy Data (Reusing the Car model we made earlier)
        val carList = listOf(
            Car(1, "Toyota Camry", "2024 • Sedan", "$45", "Available"),
            Car(2, "Honda CR-V", "2024 • SUV", "$65", "Available"),
            Car(3, "BMW Z4", "2024 • Convertible", "$120", "Available"),
            Car(4, "Tesla Model 3", "2024 • Electric", "$85", "Available")
        )

        rvCars.layoutManager = LinearLayoutManager(this)
        rvCars.adapter = CustomerCarAdapter(carList)

        // Navigation Logic
        val bottomNav = findViewById<LinearLayout>(R.id.bottom_nav)

        // Home is at index 0, already here.

        // Bookings
        bottomNav[1].setOnClickListener {
            // I'm assuming CustomerBookingsActivity exists
            startActivity(Intent(this, CustomerBookingsActivity::class.java))
        }

        // Alerts
        bottomNav[2].setOnClickListener {
            // I'm assuming CustomerAlertsActivity exists
            startActivity(Intent(this, NotificationsActivity::class.java))
        }

        // Profile
        bottomNav[3].setOnClickListener {
            // I'm assuming CustomerProfileActivity exists
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }
}
