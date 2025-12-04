// File: com/example/drivenow/NotificationsActivity.kt
package com.example.drivenow

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NotificationsActivity : AppCompatActivity() {

    private lateinit var rvNotifications: RecyclerView
    private lateinit var btnMarkRead: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        rvNotifications = findViewById(R.id.rv_notifications)
        btnMarkRead = findViewById(R.id.btn_mark_read)

        // Dummy Data based on Design
        val list = listOf(
            Notification(
                1,
                "Booking Confirmed",
                "Your booking for Toyota Camry is confirmed.",
                "2h ago",
                "booking"
            ),
            Notification(
                2,
                "Payment Successful",
                "Payment of $135 has been processed successfully.",
                "5h ago",
                "payment"
            ),
            Notification(
                3,
                "Booking Completed",
                "Your trip with Honda CR-V has ended. Please rate your experience.",
                "1d ago",
                "booking"
            ),
            Notification(
                4,
                "Welcome!",
                "Thanks for joining DriveNow. Complete your profile to start renting.",
                "2d ago",
                "alert"
            )
        )

        rvNotifications.layoutManager = LinearLayoutManager(this)
        rvNotifications.adapter = NotificationAdapter(list)

        btnMarkRead.setOnClickListener {
            Toast.makeText(this, "All notifications marked as read", Toast.LENGTH_SHORT).show()
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

        // Alerts is at index 2, already here.

        // Profile
        bottomNav[3].setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }
}