
package com.example.drivenow

import com.google.gson.annotations.SerializedName

data class CustomerBooking(
    val id: Int,
    @SerializedName("car_name") val carName: String,
    @SerializedName("car_details") val carDetails: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    val location: String,
    @SerializedName("booked_on") val bookedOn: String,
    @SerializedName("total_amount") val totalAmount: String,
    val status: String,
    @SerializedName("customer_email") val customerEmail: String
)