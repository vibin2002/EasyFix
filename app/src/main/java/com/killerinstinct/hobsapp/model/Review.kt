package com.killerinstinct.hobsapp.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class Review (
    val userUid:String = "",
    val review :String = "",
    val rating: String = "",
    val userName: String = "",
    val userProPic: String = "",
    @ServerTimestamp
    val timestamp: Timestamp = Timestamp.now(),
)