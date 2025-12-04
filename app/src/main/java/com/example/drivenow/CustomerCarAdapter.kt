// File: com/example/drivenow/CustomerCarAdapter.kt
package com.example.drivenow

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class CustomerCarAdapter(private val carList: List<Car>) : RecyclerView.Adapter<CustomerCarAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_car_title)
        val tvType: TextView = itemView.findViewById(R.id.tv_car_type)
        val tvPrice: TextView = itemView.findViewById(R.id.tv_price_val)
        val btnView: Button = itemView.findViewById(R.id.btn_view_details)
        // Add seats/fuel textviews here if your data model supports them
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_customer_car, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val car = carList[position]
        holder.tvName.text = car.name
        holder.tvType.text = car.type
        holder.tvPrice.text = car.pricePerDay

        holder.btnView.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Clicked ${car.name}", Toast.LENGTH_SHORT).show()
            // Navigate to Details Page
        }
    }

    override fun getItemCount() = carList.size
}