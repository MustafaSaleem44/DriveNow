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

        // 1. Personalize Greeting (Simplified for now - strictly fetching local name requires new API endpoint or saving it at login)
        val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userEmail = sharedPref.getString("USER_EMAIL", "") ?: ""
        
        // Use a default greeting or modify Login to save name. For now:
        tvGreeting.text = "Hello!" 

        // 2. Fetch Data
        var allCars = listOf<Car>()
        
        RetrofitClient.instance.getCars("Available").enqueue(object : retrofit2.Callback<CarResponse> {
            override fun onResponse(call: retrofit2.Call<CarResponse>, response: retrofit2.Response<CarResponse>) {
                if (response.isSuccessful && response.body()?.status == "success") {
                    allCars = response.body()?.data ?: emptyList()
                    rvCars.layoutManager = LinearLayoutManager(this@CustomerHomeActivity)
                    rvCars.adapter = CustomerCarAdapter(allCars)
                }
            }
            override fun onFailure(call: retrofit2.Call<CarResponse>, t: Throwable) {
                // Handle error
            }
        })

        // 3. Implement Search
        etSearch.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().lowercase()
                val filteredList = allCars.filter { car ->
                    car.name.lowercase().contains(query) || car.type.lowercase().contains(query)
                }
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
