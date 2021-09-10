package com.killerinstinct.hobsapp.model

import com.google.firebase.firestore.GeoPoint

data class Request(
    val requestId: String = "",
    val fromName: String = "",
    val from: String = "",
    val to: String = "",
    val description: String = "",
    val reqDate: String = "",
    val reqTime: String = "",
    val location: GeoPoint = GeoPoint(0.0,0.0),
    val contact: String = ""
)
