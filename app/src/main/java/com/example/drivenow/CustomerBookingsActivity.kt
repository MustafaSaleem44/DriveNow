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

        // Get User Email
        val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userEmail = sharedPref.getString("USER_EMAIL", "") ?: ""

        val dbHelper = DatabaseHelper(this)
        val bookingList = dbHelper.getCustomerBookings(userEmail)

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