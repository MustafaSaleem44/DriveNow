// File: com/example/drivenow/CustomerBookingsActivity.kt
package com.example.drivenow

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CustomerBookingsActivity : AppCompatActivity() {

    private lateinit var rvBookings: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_bookings)

        rvBookings = findViewById(R.id.rv_bookings)

        // Dummy Data based on Design
        val bookingList = listOf(
            CustomerBooking(
                1,
                "Toyota Camry",
                "2024 • Sedan",
                "Jan 15, 2024",
                "Jan 18, 2024",
                "Downtown Office",
                "Jan 10, 2024",
                "$135",
                "Confirmed"
            ),
            CustomerBooking(
                2,
                "Honda CR-V",
                "2024 • SUV",
                "Dec 20, 2023",
                "Dec 25, 2023",
                "Airport Terminal 1",
                "Dec 15, 2023",
                "$450",
                "Completed"
            ),
            CustomerBooking(
                3,
                "Tesla Model 3",
                "2024 • Electric",
                "Feb 01, 2024",
                "Feb 03, 2024",
                "City Center Mall",
                "Jan 28, 2024",
                "$210",
                "Active"
            )
        )

        rvBookings.layoutManager = LinearLayoutManager(this)
        rvBookings.adapter = CustomerBookingAdapter(bookingList)

        // Navigation Logic
        val bottomNav = findViewById<LinearLayout>(R.id.bottom_nav)

        // Home
        bottomNav[0].setOnClickListener {
            startActivity(Intent(this, CustomerHomeActivity::class.java))
        }

        // Bookings is at index 1, already here.

        // Alerts
        bottomNav[2].setOnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))
        }

        // Profile
        bottomNav[3].setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }
}