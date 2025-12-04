package com.example.drivenow

data class Booking(
    val id: Int,
    val customerName: String,
    val carName: String,
    val dates: String,
    val price: String,
    val status: String // "active", "completed", "cancelled"
)
