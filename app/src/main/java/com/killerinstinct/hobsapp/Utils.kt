package com.killerinstinct.hobsapp

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.killerinstinct.hobsapp.model.Job
import com.killerinstinct.hobsapp.model.Worker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.StringBuilder
import java.util.*

object Utils {

     val categories = listOf(
        "Plumber",
        "Painter",
        "Fitter",
        "Electrician",
        "Gardner",
        "Interior decorator",
        "Mason",
        "Smart appliances installer",
        "Automobile repair",
        "Two-wheeler repair"
    )

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

    fun getSpecificWorker(uid: String,isSuccessful: (Worker) -> Unit)
    {
        Log.d("KEDO", "getSpecificWorker: $uid")
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseFirestore.getInstance().collection("Worker")
                .document(uid)
                .get()
                .addOnSuccessListener {
                    isSuccessful(it.toObject(Worker::class.java)!!)
                }.addOnFailureListener {
                    Log.d("KEDO", "getSpecificWorker: $it")
                }
        }
    }

    fun sendRequest(request: Job,documentId: (String) -> Unit){
        FirebaseFirestore.getInstance()
            .collection("")
            .add(request)
            .addOnSuccessListener {
                documentId(it.id)
            }.addOnFailureListener {
                Log.d("TAG", "sendRequest: $it")
                documentId("")
            }
    }

}