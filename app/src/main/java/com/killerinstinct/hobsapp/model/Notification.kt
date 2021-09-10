package com.killerinstinct.hobsapp.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ServerTimestamp

data class Notification(
    val picUrl: String = "",
    val description: String = "",
    @ServerTimestamp
    val timestamp: Timestamp = Timestamp.now(),
    val hasRead: Boolean = false,
)
