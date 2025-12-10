package com.example.drivenow

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    val status: String,
    val message: String,
    val data: Any? = null
)

data class CarResponse(
    val status: String,
    val data: List<Car>
)

data class BookingResponse(
    val status: String,
    val data: List<CustomerBooking>
)

data class NotificationResponse(
    val status: String,
    val data: List<Notification>
)
