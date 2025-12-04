package com.example.drivenow

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CompanyDashboardActivity : AppCompatActivity() {

    private lateinit var tabOverview: TextView
    private lateinit var tabCars: TextView
    private lateinit var tabBookings: TextView

    // Parent Views for Tabs
    private lateinit var viewOverview: NestedScrollView
    private lateinit var viewCars: LinearLayout
    private lateinit var viewBookings: LinearLayout

    // RecyclerViews
    private lateinit var rvRecentBookings: RecyclerView
    private lateinit var rvCars: RecyclerView
    private lateinit var rvAllBookings: RecyclerView

    private lateinit var btnAddCar: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_dashboard)

        // Initialize Views
        tabOverview = findViewById(R.id.tab_overview)
        tabCars = findViewById(R.id.tab_cars)
        tabBookings = findViewById(R.id.tab_bookings)

        viewOverview = findViewById(R.id.view_overview)
        viewCars = findViewById(R.id.view_cars)
        viewBookings = findViewById(R.id.view_bookings)

        rvRecentBookings = findViewById(R.id.rv_recent_bookings)
        rvCars = findViewById(R.id.rv_cars)
        rvAllBookings = findViewById(R.id.rv_all_bookings)

        btnAddCar = findViewById(R.id.btn_add_car)

        // Setup RecyclerViews
        setupRecyclerViews()

        // Set Listeners
        tabOverview.setOnClickListener { switchTab("Overview") }
        tabCars.setOnClickListener { switchTab("Cars") }
        tabBookings.setOnClickListener { switchTab("Bookings") }

        btnAddCar.setOnClickListener {
            Toast.makeText(this, "Add Car clicked", Toast.LENGTH_SHORT).show()
            // Navigate to AddCarActivity
            val intent = Intent(this, AddCarActivity::class.java)
            startActivity(intent)

        }
    }

    private fun setupRecyclerViews() {
        // 1. Setup Recent Bookings (Overview)
        val recentBookings = listOf(
            Booking(1, "John Smith", "BMW X5", "Jan 15 - Jan 18", "$360", "active"),
            Booking(2, "Sarah Johnson", "Mercedes C-Class", "Jan 12 - Jan 16", "$380", "completed")
        )
        rvRecentBookings.layoutManager = LinearLayoutManager(this)
        rvRecentBookings.adapter = BookingAdapter(recentBookings)
        rvRecentBookings.isNestedScrollingEnabled = false // Optimization for NestedScrollView

        // 2. Setup Cars List
        val carList = listOf(
            Car(1, "BMW X5", "2024 • SUV", "$120/day", "available"),
            Car(2, "Mercedes C-Class", "2024 • Sedan", "$95/day", "rented"),
            Car(3, "Audi A4", "2023 • Sedan", "$85/day", "maintenance"),
            Car(4, "Tesla Model 3", "2024 • Electric", "$110/day", "available"),
            Car(5, "Toyota Camry", "2023 • Sedan", "$55/day", "available")
        )
        rvCars.layoutManager = LinearLayoutManager(this)
        rvCars.adapter = CarAdapter(carList)

        // 3. Setup All Bookings List
        val allBookings = listOf(
            Booking(1, "John Smith", "BMW X5", "Jan 15 - Jan 18", "$360", "active"),
            Booking(2, "Sarah Johnson", "Mercedes C-Class", "Jan 12 - Jan 16", "$380", "completed"),
            Booking(3, "Michael Brown", "Audi A4", "Jan 10 - Jan 12", "$170", "completed"),
            Booking(4, "Emily Davis", "Tesla Model 3", "Jan 05 - Jan 09", "$440", "cancelled")
        )
        rvAllBookings.layoutManager = LinearLayoutManager(this)
        rvAllBookings.adapter = BookingAdapter(allBookings)
    }

    private fun switchTab(tabName: String) {
        val inactiveColor = ContextCompat.getColor(this, R.color.text_secondary)
        val activeColor = ContextCompat.getColor(this, R.color.white)

        // Reset all tabs styles
        tabOverview.background = null
        tabOverview.setTextColor(inactiveColor)
        tabCars.background = null
        tabCars.setTextColor(inactiveColor)
        tabBookings.background = null
        tabBookings.setTextColor(inactiveColor)

        // Hide all views
        viewOverview.visibility = View.GONE
        viewCars.visibility = View.GONE
        viewBookings.visibility = View.GONE

        // Activate selected
        when (tabName) {
            "Overview" -> {
                tabOverview.setBackgroundResource(R.drawable.bg_tab_selected)
                tabOverview.setTextColor(activeColor)
                viewOverview.visibility = View.VISIBLE
            }
            "Cars" -> {
                tabCars.setBackgroundResource(R.drawable.bg_tab_selected)
                tabCars.setTextColor(activeColor)
                viewCars.visibility = View.VISIBLE
            }
            "Bookings" -> {
                tabBookings.setBackgroundResource(R.drawable.bg_tab_selected)
                tabBookings.setTextColor(activeColor)
                viewBookings.visibility = View.VISIBLE
            }
        }
    }
}