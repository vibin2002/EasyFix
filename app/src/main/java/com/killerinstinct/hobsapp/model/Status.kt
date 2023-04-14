package com.killerinstinct.hobsapp.model

import android.graphics.Color

enum class Status(
    val statusName :String,
    val color :Int
) {
    AVAILABLE("Available",Color.GREEN),
    AWAY("Away", Color.YELLOW),
    DND("Do not disturb", Color.DKGRAY);

    companion object{
        fun getStatusByName(name: String) : Status {
            if(name == "Available"){
                return AVAILABLE;
            }
            else if(name == "Away"){
                return AWAY;
            }
            else if(name == "Do not disturb"){
                return DND;
            }
            return AWAY;
        }
    }

}