package com.killerinstinct.hobsapp.viewmodel;

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.killerinstinct.hobsapp.Model.LoginCheck

class LoginViewModel: ViewModel()
{
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val mAuth = FirebaseAuth.getInstance()


    fun loginUser(email: String,password: String, makeToast: (String) -> Unit){
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                db.collection("LoginCheck")
                    .get().addOnCompleteListener {
                        if (it.isSuccessful)
                        {
//                            val loginCheck = it.get
//                            when {
//                                loginCheck!!.users.contains(mAuth.currentUser?.uid) -> {
//                                    Log.d("LoginChecker","Student")
//                                    makeToast("Worker")
//                                }
//                                loginCheck.workers.contains(mAuth.currentUser?.uid) -> {
//                                    Log.d("LoginChecker","Tutor")
//                                    makeToast("User")
//                                }
//                                else -> {
//                                    makeToast("Failure")
//                                }
//                            }
                        }
                        else {
                            makeToast("Failure")
                        }
                    }
            }.addOnFailureListener {
                makeToast(it.message.toString())
            }
    }
}

