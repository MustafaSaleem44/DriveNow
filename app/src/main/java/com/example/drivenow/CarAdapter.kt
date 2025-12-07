package com.example.drivenow

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class CarAdapter(
    private val carList: MutableList<Car>,
    private val onDeleteClick: (Car) -> Unit
) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgCar: android.widget.ImageView = itemView.findViewById(R.id.img_car)
        val tvName: TextView = itemView.findViewById(R.id.tv_car_name)
        val tvDetails: TextView = itemView.findViewById(R.id.tv_car_details)
        val tvPrice: TextView = itemView.findViewById(R.id.tv_price)
        val tvStatus: TextView = itemView.findViewById(R.id.tv_status_badge)
        val btnDelete: View = itemView.findViewById(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car_card_1, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val car = carList[position]

        holder.tvName.text = car.name
        holder.tvDetails.text = car.type
        holder.tvPrice.text = car.pricePerDay
        holder.tvStatus.text = car.status

        // Delete Click
        holder.btnDelete.setOnClickListener {
            onDeleteClick(car)
        }

        // Image Handling
        if (car.image.isNotEmpty()) {
            try {
                val imageBytes = android.util.Base64.decode(car.image, android.util.Base64.DEFAULT)
                val decodedImage = android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                holder.imgCar.setImageBitmap(decodedImage)
            } catch (e: Exception) {
                e.printStackTrace()
                holder.imgCar.setImageResource(R.drawable.icon_car)
            }
        } else {
             holder.imgCar.setImageResource(R.drawable.icon_car)
        }

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