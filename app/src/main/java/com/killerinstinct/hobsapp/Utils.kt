package com.killerinstinct.hobsapp

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.killerinstinct.hobsapp.model.Job
import com.killerinstinct.hobsapp.model.Notification
import com.killerinstinct.hobsapp.model.Review
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
        val sb = StringBuilder()
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(lat, long, 1)
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

    fun getSpecificWorker(uid: String, isSuccessful: (Worker) -> Unit) {
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

    fun sendRequest(request: Job, documentId: (String) -> Unit) {
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

    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    fun randomString() = (1..9)
        .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")

    fun sendNotificationToUser(
        picUrl: String,
        description: String,
        userId: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val notification = Notification(
                picUrl,
                description,
                Timestamp.now(),
                false
            )
            FirebaseFirestore.getInstance()
                .collection("User")
                .document(userId)
                .collection("Notifications")
                .add(notification)
                .addOnSuccessListener {

                }.addOnFailureListener {

                }
        }
    }

    fun sendNotificationToWorker(
        picUrl: String,
        description: String,
        workerId: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val notification = Notification(
                picUrl,
                description,
                Timestamp.now(),
                false
            )
            FirebaseFirestore.getInstance()
                .collection("Worker")
                .document(workerId)
                .collection("Notifications")
                .add(notification)
        }
    }

    fun checkReadNotifications(userId: String, ids: List<String>) {
        val db = FirebaseFirestore.getInstance()
            .collection("User")
            .document(userId)
            .collection("Notifications")
        ids.forEach {
            Log.d("Notifivation", "checkReadNotifications: $it")
            CoroutineScope(Dispatchers.IO).launch {
                db.document(it.trim())
                    .update(mapOf("hasRead" to true))
                    .addOnSuccessListener {
                        Log.d("Notifivation", "checkReadNotifications: booom")
                    }.addOnFailureListener {

                    }
            }
        }
    }

    fun checkReadNotificationsWorker(workerId: String, ids: List<String>) {
        val db = FirebaseFirestore.getInstance()
            .collection("Worker")
            .document(workerId)
            .collection("Notifications")
        ids.forEach {
            Log.d("Notifivation", "checkReadNotifications: $it")
            CoroutineScope(Dispatchers.IO).launch {
                db.document(it.trim())
                    .update(mapOf("hasRead" to true))
                    .addOnSuccessListener {
                        Log.d("Notifivation", "checkReadNotifications: booom")
                    }.addOnFailureListener {

                    }
            }
        }
    }

    fun getSpecificUserReviews(workerId: String,reviews: (List<Review>) -> Unit){
        FirebaseFirestore.getInstance()
            .collection("Worker")
            .document(workerId)
            .collection("Review")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                val list = mutableListOf<Review>()
                it.forEach { doc ->
                    val review = doc.toObject(Review::class.java)
                    list.add(review)
                }
                reviews(list.toList())
            }
    }

    fun deleteWorkerPost(workerId: String,url: String,isSuccessful: (Boolean) -> Unit){
        FirebaseFirestore.getInstance()
            .collection("Worker")
            .document(workerId)
            .collection("Posts")
            .whereEqualTo("url",url)
            .get()
            .addOnSuccessListener {
                it.forEach { post ->
                    FirebaseFirestore.getInstance()
                        .collection("Worker")
                        .document(workerId)
                        .collection("Posts")
                        .document(post.id)
                        .delete()
                        .addOnSuccessListener {
                            isSuccessful(true)
                        }.addOnFailureListener {
                            isSuccessful(false)
                        }
                }
            }.addOnFailureListener {
                isSuccessful(false)
            }
    }


}