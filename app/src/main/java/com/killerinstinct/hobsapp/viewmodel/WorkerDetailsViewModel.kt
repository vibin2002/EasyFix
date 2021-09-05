package com.killerinstinct.hobsapp.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch

class WorkerDetailsViewModel : ViewModel() {

    private val userUid = FirebaseAuth.getInstance().currentUser?.uid
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val TAG = "WorkerDetailsViewModel"

    fun updateWorkerInfo(
        location: String,
        phoneNumber: String,
        experience: String,
        minWage: String,
        category: List<String>,
        uri: Uri?,
        isCompleted: (Boolean) -> Unit
    ) {
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
                            .update("category", category)
                            .addOnSuccessListener {
                                Log.d(TAG, "updateWorkerInfo: added category")
                                uploadProPic(uri,userUid) //Upload Profile pic
                                isCompleted(true)
                            }.addOnFailureListener {
                                isCompleted(false)
                                Log.d(TAG, "updateWorkerInfo: category not added")
                            }
                    }.addOnFailureListener {
                        isCompleted(false)
                        Log.d(TAG, "updateWorkerInfo: WorkerInfo not added")
                    }
            }
        } else {
            isCompleted(false)
        }
    }

    fun uploadProPic(uri: Uri?,userUid: String){
        viewModelScope.launch {
            if (uri == null)
                return@launch
            val ref = storage.getReference("$userUid/profilePicture")
            ref.putFile(uri)
                .addOnSuccessListener {
                    Log.d(TAG, "uploadImage: Success")
                    ref.downloadUrl.addOnSuccessListener {
                        Log.d(TAG, "download url success")
                        db.collection("Worker")
                            .document(userUid)
                            .update("profilePic", it.toString())
                            .addOnSuccessListener {
                                Log.d(TAG, "image url stored in db successfully")
                            }.addOnFailureListener {
                                Log.d(TAG, "image url failed to store in db")
                            }
                    }.addOnFailureListener {
                        Log.d(TAG, "updateWorkerInfo: $it")
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, "updateWorkerInfo: $it")
                }
        }
    }

}