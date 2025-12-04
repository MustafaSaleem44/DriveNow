package com.example.drivenow

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BookingAdapter(private val bookingList: List<Booking>) : RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCustomer: TextView = itemView.findViewById(R.id.tv_customer)
        val tvCar: TextView = itemView.findViewById(R.id.tv_car)
        val tvDates: TextView = itemView.findViewById(R.id.tv_dates)
        val tvPrice: TextView = itemView.findViewById(R.id.tv_price)
        val tvStatus: TextView = itemView.findViewById(R.id.tv_status_badge)
        // Same here: ensure android:id="@+id/tv_status_badge" is added to item_booking_card_1.xml
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_booking_card_1, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookingList[position]

        holder.tvCustomer.text = booking.customerName
        holder.tvCar.text = booking.carName
        holder.tvDates.text = booking.dates
        holder.tvPrice.text = booking.price
        holder.tvStatus.text = booking.status

        // Dynamic Badge Styling (Reusing car badges for simplicity)
        when (booking.status.lowercase()) {
            "active" -> {
                holder.tvStatus.setBackgroundResource(R.drawable.badge_available) // Green
                holder.tvStatus.setTextColor(Color.BLACK)
            }
            "completed" -> {
                holder.tvStatus.setBackgroundResource(R.drawable.badge_rented) // Orangeish
                holder.tvStatus.setTextColor(Color.WHITE)
            }
            "cancelled" -> {
                holder.tvStatus.setBackgroundResource(R.drawable.badge_maintenance) // Red
                holder.tvStatus.setTextColor(Color.WHITE)
            }
        }
    }

    override fun getItemCount() = bookingList.size
}