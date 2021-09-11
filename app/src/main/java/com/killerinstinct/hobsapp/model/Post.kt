package com.killerinstinct.hobsapp.model

import com.google.firebase.Timestamp

data class Post(
    val description: String = "",
    val url: String = "",
    val date: Timestamp = Timestamp.now()
)
