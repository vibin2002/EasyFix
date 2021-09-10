package com.killerinstinct.hobsapp.model

data class Notification(
    val picUrl: String = "",
    val description: String = "",
    val timestamp: String = "",
    val isRead: Boolean = false,
)
