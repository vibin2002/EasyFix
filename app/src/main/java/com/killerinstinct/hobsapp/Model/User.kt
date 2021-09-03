package com.killerinstinct.hobsapp.Model

import com.google.firebase.firestore.GeoPoint

data class User(
    val phoneNumber: String = "",
    val userName: String = "",
    val city: String = "",
    val location:GeoPoint=GeoPoint(0.0,0.0),
    val profile: String = ""
)