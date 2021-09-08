package com.killerinstinct.hobsapp.model

import com.google.firebase.firestore.GeoPoint

data class Request(
    val from: String,
    val to: String,
    val description: String,
    val reqDateTime: String,
    val location: GeoPoint,
    val contact: String
)
