package com.killerinstinct.hobsapp.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch

class UserDetailViewModel : ViewModel() {

    val user = FirebaseAuth.getInstance().currentUser
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val TAG = "UserDetailViewModel"

    fun updateUserInfo(
        uri: Uri?,
        contact: String,
        city: String,
        location: GeoPoint,
        isCompleted: (Boolean) -> Unit
    ) {
        val map = mapOf(
            "location" to location,
            "city" to city,
            "phoneNumber" to contact,
        )
        viewModelScope.launch {
            if (user == null)
                return@launch

            db.collection("User")
                .document(user.uid)
                .update(map)
                .addOnSuccessListener {
                    if (uri == null)
                        return@addOnSuccessListener
                    uploadProPic(uri,user.uid){
                        isCompleted(true)
                    }
                }.addOnFailureListener {
                    isCompleted(true)
                }
        }
    }

    fun uploadProPic(uri: Uri?, userUid: String, isCompleted: (Boolean) -> Unit) {
        viewModelScope.launch {
            if (uri == null)
                return@launch
            val ref = storage.getReference("$userUid/profilePicture")
            ref.putFile(uri)
                .addOnSuccessListener {
                    Log.d(TAG, "uploadImage: Success")
                    ref.downloadUrl.addOnSuccessListener {
                        Log.d(TAG, "download url success")
                        db.collection("User")
                            .document(userUid)
                            .update("profile", it.toString())
                            .addOnSuccessListener {
                                Log.d(TAG, "image url stored in db successfully")
                                isCompleted(true)
                            }.addOnFailureListener {
                                isCompleted(true)
                                Log.d(TAG, "image url failed to store in db")
                            }
                    }.addOnFailureListener {
                        isCompleted(true)
                        Log.d(TAG, "updateWorkerInfo: $it")
                    }
                }
                .addOnFailureListener {
                    isCompleted(true)
                    Log.d(TAG, "updateWorkerInfo: $it")
                }
        }
    }

}