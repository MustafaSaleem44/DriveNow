// File: com/example/drivenow/CustomerBookingAdapter.kt
package com.example.drivenow

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class CustomerBookingAdapter(private val bookings: List<CustomerBooking>) : RecyclerView.Adapter<CustomerBookingAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCarName: TextView = itemView.findViewById(R.id.tv_car_name)
        val tvSpecs: TextView = itemView.findViewById(R.id.tv_car_specs)
        val tvStatus: TextView = itemView.findViewById(R.id.tv_status_badge)
        val tvDateRange: TextView = itemView.findViewById(R.id.tv_date_range)
        val tvLocation: TextView = itemView.findViewById(R.id.tv_location)
        val tvBookedOn: TextView = itemView.findViewById(R.id.tv_booked_on)
        val tvAmount: TextView = itemView.findViewById(R.id.tv_total_amount)
        val btnDetails: Button = itemView.findViewById(R.id.btn_view_details)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_customer_booking, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val booking = bookings[position]

        holder.tvCarName.text = booking.carName
        holder.tvSpecs.text = booking.carDetails
        holder.tvStatus.text = booking.status
        holder.tvDateRange.text = "${booking.startDate} - ${booking.endDate}"
        holder.tvLocation.text = booking.location
        holder.tvBookedOn.text = "Booked on ${booking.bookedOn}"
        holder.tvAmount.text = booking.totalAmount

        // Status Badge Logic
        when (booking.status) {
            "Confirmed" -> {
                holder.tvStatus.setBackgroundResource(R.drawable.badge_available) // Green
                holder.tvStatus.setTextColor(Color.BLACK)
            }
            "Active" -> {
                holder.tvStatus.setBackgroundResource(R.drawable.badge_available) // Green
                holder.tvStatus.setTextColor(Color.BLACK)
            }
            "Completed" -> {
                holder.tvStatus.setBackgroundResource(R.drawable.badge_rented) // Orange/Grey
                holder.tvStatus.setTextColor(Color.WHITE)
            }
            "Cancelled" -> {
                holder.tvStatus.setBackgroundResource(R.drawable.badge_maintenance) // Red
                holder.tvStatus.setTextColor(Color.WHITE)
            }
        }

        holder.btnDetails.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Details for ${booking.carName}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = bookings.size
}