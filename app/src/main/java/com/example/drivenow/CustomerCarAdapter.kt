// File: com/example/drivenow/CustomerCarAdapter.kt
package com.example.drivenow

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class CustomerCarAdapter(private val carList: List<Car>) : RecyclerView.Adapter<CustomerCarAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgCar: android.widget.ImageView = itemView.findViewById(R.id.img_car_large)
        val tvName: TextView = itemView.findViewById(R.id.tv_car_title)
        val tvType: TextView = itemView.findViewById(R.id.tv_car_type)
        val tvPrice: TextView = itemView.findViewById(R.id.tv_price_val)
        val tvSeats: TextView = itemView.findViewById(R.id.tv_seats) // Added
        val tvFuel: TextView = itemView.findViewById(R.id.tv_fuel)   // Added
        val btnView: Button = itemView.findViewById(R.id.btn_view_details)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_customer_car, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val car = carList[position]
        holder.tvName.text = car.name
        holder.tvType.text = car.type
        holder.tvType.text = car.type
        holder.tvPrice.text = car.pricePerDay
        holder.tvSeats.text = car.seats
        holder.tvFuel.text = car.fuelType
        
        if (car.image.isNotEmpty()) {
            try {
                val imageBytes = android.util.Base64.decode(car.image, android.util.Base64.DEFAULT)
                val decodedImage = android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                holder.imgCar.setImageBitmap(decodedImage)
                holder.imgCar.clearColorFilter() // Remove tint for real images
            } catch (e: Exception) {
                e.printStackTrace()
                holder.imgCar.setImageResource(R.drawable.icon_car)
                holder.imgCar.setColorFilter(androidx.core.content.ContextCompat.getColor(holder.itemView.context, R.color.text_secondary))
            }
        } else {
             holder.imgCar.setImageResource(R.drawable.icon_car)
             holder.imgCar.setColorFilter(androidx.core.content.ContextCompat.getColor(holder.itemView.context, R.color.text_secondary))
        }

        holder.btnView.setOnClickListener {
            // Navigate to CarDetailsActivity
            val intent = Intent(holder.itemView.context, CarDetailsActivity::class.java)
            intent.putExtra("CAR_NAME", car.name)
            intent.putExtra("CAR_TYPE", car.type)
            intent.putExtra("CAR_PRICE", car.pricePerDay)
            intent.putExtra("CAR_STATUS", car.status)
            intent.putExtra("CAR_STATUS", car.status)
            intent.putExtra("CAR_IMAGE", car.image)
            intent.putExtra("CAR_SEATS", car.seats)
            intent.putExtra("CAR_FUEL", car.fuelType)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = carList.size
}