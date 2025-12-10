package com.example.drivenow

import com.google.gson.annotations.SerializedName

data class Car(
    val id: Int,
    val name: String,
    val type: String,
    @SerializedName("price") val pricePerDay: String,
    val status: String,
    val image: String,
    val seats: String,
    @SerializedName("fuel") val fuelType: String
)