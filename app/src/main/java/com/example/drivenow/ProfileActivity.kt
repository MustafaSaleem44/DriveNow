// File: com/example/drivenow/ProfileActivity.kt
package com.example.drivenow

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize Logout Button
        val btnLogout = findViewById<LinearLayout>(R.id.btn_logout)
        btnLogout.setOnClickListener {
            // Clear user session here
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()

            // Navigate back to Login
            val intent = Intent(this, LoginActivity::class.java)
            // Clear back stack so user can't press back to return to profile
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // Navigation Logic
        val bottomNav = findViewById<LinearLayout>(R.id.bottom_nav)

        // Home
        bottomNav[0].setOnClickListener {
            startActivity(Intent(this, CustomerHomeActivity::class.java))
        }

        // Bookings
        bottomNav[1].setOnClickListener {
            startActivity(Intent(this, CustomerBookingsActivity::class.java))
        }

        // Alerts
        bottomNav[2].setOnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))
        }

        // Profile is at index 3, already here.
    }
}