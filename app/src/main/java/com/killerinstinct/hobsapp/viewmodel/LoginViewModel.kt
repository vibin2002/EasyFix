package com.killerinstinct.hobsapp.viewmodel;

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.killerinstinct.hobsapp.model.LoginCheck

class LoginViewModel: ViewModel()  {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val mAuth = FirebaseAuth.getInstance()


    fun loginUser(email: String,password: String, makeToast: (String) -> Unit){
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                db.collection("LoginCheck")
                    .document("LoginCheck")
                    .get().addOnCompleteListener {
                        if (it.isSuccessful) {
                            val loginCheck = it.result?.toObject(LoginCheck::class.java)
                            when {
                                loginCheck!!.users.contains(mAuth.currentUser?.uid) -> {
                                    Log.d("LoginChecker","User")
                                    makeToast("User")
                                }
                                loginCheck.workers.contains(mAuth.currentUser?.uid) -> {
                                    Log.d("LoginChecker","Worker")
                                    makeToast("Worker")
                                }
                                else -> {
                                    makeToast("Failure")
                                }
                            }
                        } else {
                            makeToast("Failure")
                        }
                    }
            }.addOnFailureListener {
                makeToast("Failure")
            }
    }

}
