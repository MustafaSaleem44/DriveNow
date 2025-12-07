package com.example.drivenow

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.drivenow.data.AppDatabase
import com.example.drivenow.repository.BookingRepository
import com.example.drivenow.repository.CarRepository
import com.example.drivenow.utils.SharedPreferencesHelper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CompanyDashboardOverview : AppCompatActivity() {
    private lateinit var totalCarsText: TextView
    private lateinit var totalBookingsText: TextView
    private lateinit var monthlyRevenueText: TextView
    private lateinit var ratingText: TextView
    private lateinit var addCarButton: View
    
    private lateinit var carRepository: CarRepository
    private lateinit var bookingRepository: BookingRepository
    private lateinit var prefsHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_dashboard_overview)

        val database = AppDatabase.getDatabase(this)
        val companyId = SharedPreferencesHelper(this).getUserId()
        
        if (companyId == null) {
            finish()
            return
        }

        carRepository = CarRepository(database.carDao(), this)
        bookingRepository = BookingRepository(database.bookingDao(), this)
        prefsHelper = SharedPreferencesHelper(this)

        initializeViews()
        setupClickListeners()
        loadDashboardData(companyId)
    }

    private fun initializeViews() {
        totalCarsText = findViewById(R.id.text_total_cars)
        totalBookingsText = findViewById(R.id.text_total_bookings)
        monthlyRevenueText = findViewById(R.id.text_monthly_revenue)
        ratingText = findViewById(R.id.text_rating)
        addCarButton = findViewById(R.id.button_add_car)

        findViewById<View>(R.id.button_back).setOnClickListener {
            finish()
        }
    }

    private fun setupClickListeners() {
        findViewById<TextView>(R.id.text_overview).setOnClickListener { /* Already on Overview */ }
        findViewById<TextView>(R.id.text_cars).setOnClickListener {
            startActivity(Intent(this, CompanyDashboardCars::class.java))
        }
        findViewById<TextView>(R.id.text_bookings).setOnClickListener {
            startActivity(Intent(this, CompanyDashboardBookings::class.java))
        }

        addCarButton.setOnClickListener {
            startActivity(Intent(this, AddCar::class.java))
        }
    }

    private fun loadDashboardData(companyId: String) {
        lifecycleScope.launch {
            // Load total cars
            carRepository.getCarsByCompany(companyId).collect { cars ->
                totalCarsText.text = cars.size.toString()
            }

            // Load total bookings
            bookingRepository.getBookingsByCompany(companyId).collect { bookings ->
                totalBookingsText.text = bookings.size.toString()
                
                // Calculate monthly revenue
                val monthlyBookings = bookings.filter { booking ->
                    val bookingMonth = java.util.Calendar.getInstance().apply {
                        timeInMillis = booking.createdAt
                    }.get(java.util.Calendar.MONTH)
                    val currentMonth = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH)
                    bookingMonth == currentMonth
                }
                val revenue = monthlyBookings.sumOf { it.totalAmount }
                monthlyRevenueText.text = "$${String.format("%.0f", revenue)}"
            }

            // Set default rating
            ratingText.text = "4.8"
        }
    }
}