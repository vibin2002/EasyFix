package com.killerinstinct.hobsapp.model

import android.graphics.Color
import com.killerinstinct.hobsapp.R

enum class Status(
    val index: Int,
    val statusName :String,
    val color :Int,
    val drawable :Int
) {
    AVAILABLE(0,"Available",Color.GREEN, R.drawable.ic_circle_green),
    AWAY(1,"Away", Color.YELLOW, R.drawable.ic_away),
    MEETING(2,"In a meeting", Color.MAGENTA, R.drawable.ic_inameeting),
    DND(3,"Do not disturb", Color.RED, R.drawable.ic_donotdisturb),
    ONLEAVE(4,"On leave", Color.DKGRAY, R.drawable.ic_onleave);

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
            else if(name == "In a meeting") {
                return MEETING;
            }
            else if(name == "On leave") {
                return ONLEAVE;
            }
            return AWAY;
        }
    }

}