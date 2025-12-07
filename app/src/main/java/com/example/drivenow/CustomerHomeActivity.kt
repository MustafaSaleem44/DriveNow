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
        val tvGreeting = findViewById<android.widget.TextView>(R.id.tv_greeting_name)
        val etSearch = findViewById<android.widget.EditText>(R.id.et_search_cars)

        // 1. Personalize Greeting
        val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userEmail = sharedPref.getString("USER_EMAIL", "") ?: ""
        val dbHelper = DatabaseHelper(this)
        
        if (userEmail.isNotEmpty()) {
            val userName = dbHelper.getCustomerName(userEmail)
            tvGreeting.text = "Hello, $userName!"
        }

        // 2. Fetch Data
        val allCars = dbHelper.getAllAvailableCars()
        
        rvCars.layoutManager = LinearLayoutManager(this)
        val adapter = CustomerCarAdapter(allCars)
        rvCars.adapter = adapter

        // 3. Implement Search
        etSearch.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().lowercase()
                val filteredList = allCars.filter { car ->
                    car.name.lowercase().contains(query) || car.type.lowercase().contains(query)
                }
                // Update adapter with filtered list
                // Note: It's better to update data inside adapter, but creating new one works for simple lists
                 rvCars.adapter = CustomerCarAdapter(filteredList)
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })

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
