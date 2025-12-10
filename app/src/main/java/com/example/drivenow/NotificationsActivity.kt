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

        // Fetch Real Notifications
        // Fetch Real Notifications
        val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userEmail = sharedPref.getString("USER_EMAIL", "") ?: ""
        
        RetrofitClient.instance.getNotifications(userEmail).enqueue(object : retrofit2.Callback<NotificationResponse> {
            override fun onResponse(call: retrofit2.Call<NotificationResponse>, response: retrofit2.Response<NotificationResponse>) {
                if (response.isSuccessful && response.body()?.status == "success") {
                    val notificationList = response.body()?.data ?: emptyList()
                    rvNotifications.layoutManager = LinearLayoutManager(this@NotificationsActivity)
                    rvNotifications.adapter = NotificationAdapter(notificationList)
                }
            }

            override fun onFailure(call: retrofit2.Call<NotificationResponse>, t: Throwable) {
                Toast.makeText(this@NotificationsActivity, "Error loading notifications", Toast.LENGTH_SHORT).show()
            }
        })

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