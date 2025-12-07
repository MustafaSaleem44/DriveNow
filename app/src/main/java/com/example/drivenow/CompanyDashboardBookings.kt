package com.example.drivenow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drivenow.data.AppDatabase
import com.example.drivenow.data.Booking
import com.example.drivenow.repository.BookingRepository
import com.example.drivenow.utils.SharedPreferencesHelper
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CompanyDashboardBookings : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var bookingRepository: BookingRepository
    private lateinit var prefsHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_dashboard_bookings)

        val database = AppDatabase.getDatabase(this)
        bookingRepository = BookingRepository(database.bookingDao(), this)
        prefsHelper = SharedPreferencesHelper(this)

        initializeViews()
        setupClickListeners()
        loadBookings()
    }

    private fun initializeViews() {
        recyclerView = findViewById(R.id.recycler_bookings)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupClickListeners() {
        findViewById<TextView>(R.id.text_overview).setOnClickListener {
            startActivity(Intent(this, CompanyDashboardOverview::class.java))
        }

        findViewById<TextView>(R.id.text_cars).setOnClickListener {
            startActivity(Intent(this, CompanyDashboardCars::class.java))
        }

        findViewById<View>(R.id.button_back).setOnClickListener {
            finish()
        }
    }

    private fun loadBookings() {
        val companyId = prefsHelper.getUserId()
        if (companyId == null) {
            finish()
            return
        }

        lifecycleScope.launch {
            bookingRepository.getBookingsByCompany(companyId).collect { bookings ->
                recyclerView.adapter = CompanyBookingAdapter(bookings.reversed())
            }
        }
    }

    private class CompanyBookingAdapter(
        private val bookings: List<Booking>
    ) : RecyclerView.Adapter<CompanyBookingAdapter.BookingViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_company_booking, parent, false)
            return BookingViewHolder(view)
        }

        override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
            holder.bind(bookings[position])
        }

        override fun getItemCount() = bookings.size

        inner class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val customerName: TextView = itemView.findViewById(R.id.text_customer_name)
            private val carName: TextView = itemView.findViewById(R.id.text_car_name)
            private val dateRange: TextView = itemView.findViewById(R.id.text_date_range)
            private val priceText: TextView = itemView.findViewById(R.id.text_price)
            private val statusBadge: TextView = itemView.findViewById(R.id.text_status)

            fun bind(booking: Booking) {
                // Get customer name from user ID (simplified - should fetch from API)
                customerName.text = "Customer"
                carName.text = booking.carName

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val startDate = dateFormat.format(Date(booking.startDate))
                val endDate = dateFormat.format(Date(booking.endDate))
                dateRange.text = "$startDate - $endDate"

                priceText.text = "$${String.format("%.0f", booking.totalAmount)}"

                // Status badge
                statusBadge.text = booking.status.name.lowercase().capitalize()
                val statusColor = when (booking.status) {
                    com.example.drivenow.data.BookingStatus.ACTIVE -> 0xFFBB86FC.toInt()
                    com.example.drivenow.data.BookingStatus.COMPLETED -> 0xFF03DAC5.toInt()
                    com.example.drivenow.data.BookingStatus.CONFIRMED -> 0xFFBB86FC.toInt()
                    else -> 0xFFB0B0B0.toInt()
                }
                statusBadge.setBackgroundColor(statusColor)
            }
        }
    }
}