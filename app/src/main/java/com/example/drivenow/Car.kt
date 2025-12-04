package com.example.drivenow

data class Car(
    val id: Int,
    val name: String,
    val type: String,
    val pricePerDay: String,
    val status: String // "available", "rented", "maintenance"
)