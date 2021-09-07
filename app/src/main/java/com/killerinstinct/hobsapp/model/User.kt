package com.killerinstinct.hobsapp.model

import com.google.firebase.firestore.GeoPoint

data class User(
    val uid: String = "",
    val phoneNumber: String = "",
    val userName: String = "",
    val city: String = "",
    val location:GeoPoint=GeoPoint(0.0,0.0),
    val profile: String = "",
    val requests : List<Job> = listOf(),
    val jobs: List<Job> = listOf()
)