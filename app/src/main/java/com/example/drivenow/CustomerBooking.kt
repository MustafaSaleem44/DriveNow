
package com.example.drivenow

data class CustomerBooking(
    val id: Int,
    val carName: String,
    val carDetails: String, // e.g., "2024 • Sedan"
    val startDate: String,
    val endDate: String,
    val location: String,
    val bookedOn: String,
    val totalAmount: String,
    val status: String // "Confirmed", "Active", "Completed", "Cancelled"
)