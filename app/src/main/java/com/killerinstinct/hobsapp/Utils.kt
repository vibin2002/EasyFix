package com.killerinstinct.hobsapp

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import java.lang.StringBuilder
import java.util.*

object Utils {

    fun getLocationAddress(lat: Double, long: Double, context: Context): String {
        var sb = StringBuilder()
        val geocoder = Geocoder(context, Locale.getDefault())
        var addresses: List<Address> = geocoder.getFromLocation(lat, long, 1)
//            binding.location.text = addresses[0].locality+","+addresses[0].
        try {

            if (addresses.size > 0) {
                Log.e("Location Add", "$addresses")
                var address: Address = addresses[0]
                sb.append(address.locality).append(" ").append(",").append(address.countryName)
            }
        } catch (e: Exception) {
            Log.e("Errorin", e.message.toString())
        }
        return sb.toString()
    }


}