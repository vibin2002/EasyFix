package com.killerinstinct.hobsapp.model

import com.google.firebase.firestore.GeoPoint

data class Worker (
    val phoneNumber:String="",
    val userName:String="",
    val email:String="",
    val experience:String="",
    val category:List<String> = listOf(),
    val rating:String="",
    val ratersCount:String="",
    val reviews: List<String> = listOf(),
    val profilePic:String="",
    val location: GeoPoint=GeoPoint(11.0168,76.9558),
    val minWage:String="",
    val posts: List<String> = listOf()
    )