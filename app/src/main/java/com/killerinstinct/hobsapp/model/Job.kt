package com.killerinstinct.hobsapp.model

import com.google.firebase.firestore.GeoPoint

data class Job(
    val jobId: String = "",
    val fromName: String = "",
    val fromId: String = "",
    val toId: String = "",
    val description: String = "",
    val reqDate: String = "",
    val reqTime: String = "",
    val location: GeoPoint = GeoPoint(0.0,0.0),
    val contact: String = "",
    val completed: Boolean = false
)
