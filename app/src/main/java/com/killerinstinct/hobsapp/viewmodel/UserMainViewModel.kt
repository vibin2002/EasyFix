package com.killerinstinct.hobsapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.killerinstinct.hobsapp.model.Request
import com.killerinstinct.hobsapp.model.User
import com.killerinstinct.hobsapp.model.Worker
import kotlinx.coroutines.launch

class UserMainViewModel: ViewModel() {

    private val TAG = "UserMainViewModel"
    private val userUid = FirebaseAuth.getInstance().currentUser?.uid
    private val db = FirebaseFirestore.getInstance()

    private val _allWorkers: MutableLiveData<MutableList<Worker>> = MutableLiveData()
    val allWorkers: LiveData<MutableList<Worker>> = _allWorkers

    private val _user: MutableLiveData<User> = MutableLiveData()
    val user: LiveData<User> = _user

    fun getAllWorkers(){
        viewModelScope.launch {
            val workers = mutableListOf<Worker>()
            db.collection("Worker")
                .get()
                .addOnSuccessListener { documents ->
                    for (doc in documents){
                        val worker = doc.toObject(Worker::class.java)
                        workers.add(worker)
                    }
                    _allWorkers.value = workers
                    Log.d(TAG, "getAllWorkers: Obtained all workers")
                }.addOnFailureListener {
                    Log.d(TAG, "getAllWorkers: $it")
                }
        }
    }

    fun fetchUserDetail(){
        viewModelScope.launch {
            if (userUid == null)
                return@launch
            db.collection("User")
                .document(userUid)
                .get()
                .addOnSuccessListener {
                    _user.value = it.toObject(User::class.java)
                }.addOnFailureListener {
                    Log.d(TAG, "fetchUserDetail: User not fetched")
                }
        }
    }

    fun sendWorkRequest(
        fromId: String,
        toId: String,
        description: String,
        location: GeoPoint,
        dateTime: String,
        contact: String
    ){
        val request = Request(
            fromId,
            toId,
            description,
            "",
            location,
            contact
        )
        db.collection("Request")
            .add(request)
            .addOnSuccessListener {
                val reqId = it.id
                db.collection("Worker")
                    .document(toId)
                    .update("requests",FieldValue.arrayUnion(reqId))
                    .addOnSuccessListener {
                        Log.d(TAG, "sendWorkRequest: Request added")

                    }.addOnFailureListener {
                        Log.d(TAG, "sendWorkRequest: Request not sent")
                    }
            }

    }




}