package com.example.drivenow

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
import com.example.drivenow.data.Notification
import com.example.drivenow.data.dao.NotificationDao
import com.example.drivenow.utils.SharedPreferencesHelper
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class Notifications : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var notificationDao: NotificationDao
    private lateinit var prefsHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        val database = AppDatabase.getDatabase(this)
        notificationDao = database.notificationDao()
        prefsHelper = SharedPreferencesHelper(this)

        initializeViews()
        loadNotifications()
    }

    private fun initializeViews() {
        recyclerView = findViewById(R.id.recycler_notifications)
        recyclerView.layoutManager = LinearLayoutManager(this)

        findViewById<View>(R.id.button_back).setOnClickListener {
            finish()
        }

        findViewById<TextView>(R.id.text_mark_all_read).setOnClickListener {
            markAllAsRead()
        }
    }

    private fun loadNotifications() {
        val userId = prefsHelper.getUserId()
        if (userId == null) {
            finish()
            return
        }

        lifecycleScope.launch {
            notificationDao.getNotificationsByUser(userId).collect { notifications ->
                recyclerView.adapter = NotificationAdapter(notifications.reversed()) { notification ->
                    markAsRead(notification.id)
                }
            }
        }
    }

    private fun markAsRead(notificationId: String) {
        lifecycleScope.launch {
            notificationDao.markAsRead(notificationId)
        }
    }

    private fun markAllAsRead() {
        val userId = prefsHelper.getUserId() ?: return
        lifecycleScope.launch {
            notificationDao.markAllAsRead(userId)
        }
    }

    private class NotificationAdapter(
        private val notifications: List<Notification>,
        private val onNotificationClick: (Notification) -> Unit
    ) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_notification, parent, false)
            return NotificationViewHolder(view)
        }

        override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
            holder.bind(notifications[position])
        }

        override fun getItemCount() = notifications.size

        inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val titleText: TextView = itemView.findViewById(R.id.text_title)
            private val messageText: TextView = itemView.findViewById(R.id.text_message)
            private val timeText: TextView = itemView.findViewById(R.id.text_time)
            private val unreadIndicator: View = itemView.findViewById(R.id.view_unread_indicator)

            fun bind(notification: Notification) {
                titleText.text = notification.title
                messageText.text = notification.message

                // Format time
                val now = System.currentTimeMillis()
                val diff = now - notification.createdAt
                val days = (diff / (1000 * 60 * 60 * 24)).toInt()
                timeText.text = if (days == 0) {
                    "Today"
                } else if (days == 1) {
                    "Yesterday"
                } else {
                    "${days}d ago"
                }

                // Show unread indicator
                unreadIndicator.visibility = if (notification.isRead) View.GONE else View.VISIBLE

                itemView.setOnClickListener {
                    onNotificationClick(notification)
                }
            }
        }
    }
}