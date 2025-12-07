package com.example.drivenow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.drivenow.data.AppDatabase
import com.example.drivenow.data.Booking
import com.example.drivenow.repository.BookingRepository
import com.example.drivenow.utils.SharedPreferencesHelper
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class Bookings : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var bookingRepository: BookingRepository
    private lateinit var prefsHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookings)

        val database = AppDatabase.getDatabase(this)
        bookingRepository = BookingRepository(database.bookingDao(), this)
        prefsHelper = SharedPreferencesHelper(this)

        initializeViews()
        loadBookings()
    }

    private fun initializeViews() {
        recyclerView = findViewById(R.id.recycler_bookings)
        recyclerView.layoutManager = LinearLayoutManager(this)

        findViewById<View>(R.id.button_back).setOnClickListener {
            finish()
        }
    }

    private fun loadBookings() {
        val userId = prefsHelper.getUserId()
        if (userId == null) {
            finish()
            return
        }

        lifecycleScope.launch {
            // Refresh from server
            bookingRepository.refreshBookingsFromServer(userId)

            // Observe bookings
            bookingRepository.getBookingsByUser(userId).collect { bookings ->
                recyclerView.adapter = BookingAdapter(bookings.reversed()) { booking ->
                    // Handle booking click if needed
                }
            }
        }
    }

    private class BookingAdapter(
        private val bookings: List<Booking>,
        private val onBookingClick: (Booking) -> Unit
    ) : RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_booking, parent, false)
            return BookingViewHolder(view)
        }

        override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
            holder.bind(bookings[position])
        }

        override fun getItemCount() = bookings.size

        inner class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val carImage: ImageView = itemView.findViewById(R.id.image_car)
            private val carName: TextView = itemView.findViewById(R.id.text_car_name)
            private val carDetails: TextView = itemView.findViewById(R.id.text_car_details)
            private val dateRange: TextView = itemView.findViewById(R.id.text_date_range)
            private val location: TextView = itemView.findViewById(R.id.text_location)
            private val bookedDate: TextView = itemView.findViewById(R.id.text_booked_date)
            private val totalAmount: TextView = itemView.findViewById(R.id.text_total_amount)
            private val statusBadge: TextView = itemView.findViewById(R.id.text_status)
            private val viewDetailsButton: TextView = itemView.findViewById(R.id.button_view_details)

            fun bind(booking: Booking) {
                carName.text = booking.carName
                
                val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                val startDate = dateFormat.format(Date(booking.startDate))
                val endDate = dateFormat.format(Date(booking.endDate))
                dateRange.text = "$startDate - $endDate"
                
                location.text = booking.pickupLocation
                
                val bookedDateStr = dateFormat.format(Date(booking.createdAt))
                bookedDate.text = "Booked on $bookedDateStr"
                
                totalAmount.text = "$${String.format("%.2f", booking.totalAmount)}"

                // Status badge
                statusBadge.text = booking.status.name
                val statusColor = when (booking.status) {
                    com.example.drivenow.data.BookingStatus.CONFIRMED -> 0xFF03DAC5.toInt()
                    com.example.drivenow.data.BookingStatus.ACTIVE -> 0xFFBB86FC.toInt()
                    com.example.drivenow.data.BookingStatus.COMPLETED -> 0xFF03DAC5.toInt()
                    else -> 0xFFB0B0B0.toInt()
                }
                statusBadge.setTextColor(statusColor)

                // Load car image (placeholder for now)
                carImage.setImageResource(R.drawable.image_fallback)

                viewDetailsButton.setOnClickListener {
                    onBookingClick(booking)
                }
            }
        }
    }
}