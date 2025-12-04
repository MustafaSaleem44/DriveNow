package com.example.drivenow

data class Notification(
    val id: Int,
    val title: String,
    val message: String,
    val timeAgo: String,
    val type: String // "booking", "payment", "alert"
)