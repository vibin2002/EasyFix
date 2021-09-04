package com.killerinstinct.hobsapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class WorkerDetailsViewModel: ViewModel() {

    private val userUid = FirebaseAuth.getInstance().currentUser?.uid
    private val db = FirebaseFirestore.getInstance()
    private val TAG = "WorkerDetailsViewModel"

    fun updateWorkerInfo(
        location: String,
        phoneNumber: String,
        experience: String,
        minWage: String,
        category: List<String>
    ): Boolean {
        val workerData = mapOf(
            "location" to location,
            "phoneNumber" to phoneNumber,
            "experience" to experience,
            "minWage" to minWage,
        )
        if (userUid != null) {
            viewModelScope.launch {
                db.collection("Worker")
                    .document(userUid)
                    .update(workerData)
                    .addOnSuccessListener {
                        Log.d(TAG, "updateWorkerInfo: successfully added")
                        db.collection("Worker")
                            .document(userUid)
                            .update("category",category)
                            .addOnSuccessListener {
                                Log.d(TAG, "updateWorkerInfo: added category")
                            }.addOnFailureListener {
                                Log.d(TAG, "updateWorkerInfo: category not added")
                            }
                    }.addOnFailureListener {
                        Log.d(TAG, "updateWorkerInfo: WorkerInfo not added")
                    }
            }
            return true
        }
        else
        {
            return false
        }
    }


}