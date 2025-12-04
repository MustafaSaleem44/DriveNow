package com.example.drivenow

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class CarAdapter(private val carList: List<Car>) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_car_name)
        val tvDetails: TextView = itemView.findViewById(R.id.tv_car_details)
        val tvPrice: TextView = itemView.findViewById(R.id.tv_price)
        val tvStatus: TextView = itemView.findViewById(R.id.tv_status_badge)
        // Ensure "badge_available" is the id in your item_car_card_1.xml for the status textview,
        // if it didn't have an ID, please add android:id="@+id/tv_status_badge" to that TextView.
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car_card_1, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val car = carList[position]
        val context = holder.itemView.context

        holder.tvName.text = car.name
        holder.tvDetails.text = car.type
        holder.tvPrice.text = car.pricePerDay
        holder.tvStatus.text = car.status

        // Dynamic Badge Styling
        when (car.status.lowercase()) {
            "available" -> {
                holder.tvStatus.setBackgroundResource(R.drawable.badge_available)
                holder.tvStatus.setTextColor(Color.BLACK)
            }
            "rented" -> {
                holder.tvStatus.setBackgroundResource(R.drawable.badge_rented)
                holder.tvStatus.setTextColor(Color.WHITE)
            }
            "maintenance" -> {
                holder.tvStatus.setBackgroundResource(R.drawable.badge_maintenance)
                holder.tvStatus.setTextColor(Color.WHITE)
            }
        }
    }

    override fun getItemCount() = carList.size
}