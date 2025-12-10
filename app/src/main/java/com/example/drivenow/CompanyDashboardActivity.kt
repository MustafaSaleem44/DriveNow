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
        fetchData()
    }

    private fun fetchData() {
        // 1. Fetch Cars
        RetrofitClient.instance.getCars().enqueue(object : retrofit2.Callback<CarResponse> {
            override fun onResponse(call: retrofit2.Call<CarResponse>, response: retrofit2.Response<CarResponse>) {
                if (response.isSuccessful && response.body()?.status == "success") {
                    val carList = response.body()?.data ?: emptyList()
                    val mutableCars = carList.toMutableList()

                    // Setup Car Adapter
                    rvCars.layoutManager = LinearLayoutManager(this@CompanyDashboardActivity)
                    rvCars.adapter = CarAdapter(mutableCars) { car ->
                        deleteCar(car.id)
                    }

                    // 2. Fetch Bookings (Chained)
                    fetchBookings(mutableCars.size)
                } else {
                    Toast.makeText(this@CompanyDashboardActivity, "Failed to load cars", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<CarResponse>, t: Throwable) {
                Toast.makeText(this@CompanyDashboardActivity, "Error loading cars: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchBookings(totalCars: Int) {
        RetrofitClient.instance.getBookings().enqueue(object : retrofit2.Callback<BookingResponse> {
            override fun onResponse(call: retrofit2.Call<BookingResponse>, response: retrofit2.Response<BookingResponse>) {
                if (response.isSuccessful && response.body()?.status == "success") {
                    val allBookings = response.body()?.data ?: emptyList()
                    
                    // Setup Recent Bookings
                    val recentBookings = allBookings.take(3)
                    rvRecentBookings.layoutManager = LinearLayoutManager(this@CompanyDashboardActivity)
                    rvRecentBookings.adapter = BookingAdapter(recentBookings)
                    rvRecentBookings.isNestedScrollingEnabled = false

                    // Setup All Bookings
                    rvAllBookings.layoutManager = LinearLayoutManager(this@CompanyDashboardActivity)
                    rvAllBookings.adapter = BookingAdapter(allBookings)

                    // Update Stats
                    updateDashboardStats(totalCars, allBookings)
                }
            }

            override fun onFailure(call: retrofit2.Call<BookingResponse>, t: Throwable) {
                Toast.makeText(this@CompanyDashboardActivity, "Error loading bookings", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteCar(id: Int) {
        RetrofitClient.instance.deleteCar(id).enqueue(object : retrofit2.Callback<ApiResponse> {
            override fun onResponse(call: retrofit2.Call<ApiResponse>, response: retrofit2.Response<ApiResponse>) {
                if (response.isSuccessful && response.body()?.status == "success") {
                    Toast.makeText(this@CompanyDashboardActivity, "Car Deleted", Toast.LENGTH_SHORT).show()
                    fetchData() // Refresh
                } else {
                    Toast.makeText(this@CompanyDashboardActivity, "Failed to delete", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<ApiResponse>, t: Throwable) {
                Toast.makeText(this@CompanyDashboardActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
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