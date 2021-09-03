package com.killerinstinct.hobsapp.model

data class LoginCheck(
    val users: List<String> = listOf(),
    val workers: List<String> = listOf()
)
