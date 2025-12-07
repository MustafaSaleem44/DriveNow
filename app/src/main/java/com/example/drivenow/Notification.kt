package com.example.drivenow

data class Notification(
    val id: Int = -1,
    val title: String,
    val message: String,
    val date: String,
    val userEmail: String, // To filter by user
    val type: String // e.g. "Payment", "Booking", "System"
)