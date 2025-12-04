// File: com/example/drivenow/NotificationAdapter.kt
package com.example.drivenow

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotificationAdapter(private val notifications: List<Notification>) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvMessage: TextView = itemView.findViewById(R.id.tv_message)
        val tvTime: TextView = itemView.findViewById(R.id.tv_time)
        val imgIcon: ImageView = itemView.findViewById(R.id.img_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification = notifications[position]

        holder.tvTitle.text = notification.title
        holder.tvMessage.text = notification.message
        holder.tvTime.text = notification.timeAgo

        // Icon Logic
        when (notification.type) {
            "booking" -> holder.imgIcon.setImageResource(R.drawable.ic_check_circle)
            "payment" -> holder.imgIcon.setImageResource(R.drawable.ic_payment_success)
            else -> holder.imgIcon.setImageResource(R.drawable.ic_notification)
        }
    }

    override fun getItemCount() = notifications.size
}