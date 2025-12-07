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
        val dbHelper = DatabaseHelper(this)

        // 1. Setup Recent Bookings (Overview) - showing first 3 for example
        val allBookingsList = dbHelper.getAllBookings()
        val recentBookings = allBookingsList.take(3)
        rvRecentBookings.layoutManager = LinearLayoutManager(this)
        rvRecentBookings.adapter = BookingAdapter(recentBookings)
        rvRecentBookings.isNestedScrollingEnabled = false 

        // 2. Setup Cars List
        // 2. Setup Cars List
        val carList = dbHelper.getAllCars().toMutableList()
        rvCars.layoutManager = LinearLayoutManager(this)
        rvCars.adapter = CarAdapter(carList) { carToDelete ->
            // Delete Logic
             if (dbHelper.deleteCar(carToDelete.id) > 0) {
                 Toast.makeText(this, "${carToDelete.name} deleted", Toast.LENGTH_SHORT).show()
                 // Refresh List
                 val updatedList = dbHelper.getAllCars().toMutableList()
                 rvCars.adapter = CarAdapter(updatedList) { car ->
                     // Recursive callback for the refreshed adapter
                     if (dbHelper.deleteCar(car.id) > 0) {
                         Toast.makeText(this, "${car.name} deleted", Toast.LENGTH_SHORT).show()
                         setupRecyclerViews() // Simple refresh
                     }
                 }
                 // Also Update Stats
                 val bookings = dbHelper.getAllBookings()
                 updateDashboardStats(updatedList.size, bookings)
             } else {
                 Toast.makeText(this, "Failed to delete", Toast.LENGTH_SHORT).show()
             }
        }

        // 3. Setup All Bookings List
        rvAllBookings.layoutManager = LinearLayoutManager(this)
        rvAllBookings.adapter = BookingAdapter(allBookingsList)

        // 4. Update Dashboard Stats
        updateDashboardStats(carList.size, allBookingsList)
    }

    private fun updateDashboardStats(totalCars: Int, bookings: List<CustomerBooking>) {
        val tvTotalCars = findViewById<TextView>(R.id.tv_stat_total_cars)
        val tvTotalBookings = findViewById<TextView>(R.id.tv_stat_total_bookings)
        val tvRevenue = findViewById<TextView>(R.id.tv_stat_revenue)
        val tvRating = findViewById<TextView>(R.id.tv_stat_rating)

        // Count
        tvTotalCars.text = totalCars.toString()
        tvTotalBookings.text = bookings.size.toString()

        // Revenue Calculation
        var totalRevenue = 0.0
        for (booking in bookings) {
            try {
                // Format is usually "$123.45"
                val priceStr = booking.totalAmount.replace("$", "").replace(",", "").trim()
                if (priceStr.isNotEmpty()) {
                    totalRevenue += priceStr.toDouble()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        tvRevenue.text = "$${String.format("%,.0f", totalRevenue)}"

        // Rating (Placeholder Logic as no rating system exists yet)
        tvRating.text = "5.0" 
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