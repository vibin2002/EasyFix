package com.killerinstinct.hobsapp.model

import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Worker (
    val uid: String = "",
    val phoneNumber:String="",
    val userName:String="",
    val email:String="",
    val experience:String="",
    val category:List<String> = listOf(),
    val rating:String="",
    val ratersCount:String="",
    val reviews: List<String> = listOf(),
    val profilePic:String="",
    val location: @RawValue GeoPoint=GeoPoint(0.0,0.0),
    val minWage:String="",
    val posts: List<String> = listOf()
    ): Parcelable