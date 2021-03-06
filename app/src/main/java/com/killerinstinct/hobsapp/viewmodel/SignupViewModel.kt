package com.killerinstinct.hobsapp.viewmodel

import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.Utils
import com.killerinstinct.hobsapp.model.User
import com.killerinstinct.hobsapp.model.Worker
import kotlinx.coroutines.launch

class SignupViewModel: ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun signUpWithCustomAuth(
        username:String,
        email:String,
        password:String,
        designation:String,
        makeToast: (String)->Unit
    ) {
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    //  if user created
                    val uid = it.user?.uid
                    if (uid == null)
                        return@addOnSuccessListener
                    if (designation == "Worker"){
                        val worker = Worker(uid = uid,email = email,userName = username)
                        auth.currentUser?.let { it1 ->
                            db.collection("Worker")
                                .document(it1.uid)
                                .set(worker)
                                .addOnSuccessListener {
                                    db.collection("LoginCheck")
                                        .document("LoginCheck").update("workers", FieldValue.arrayUnion(it1.uid))
                                    Utils.sendNotificationToWorker(
                                        "https://firebasestorage.googleapis.com/v0/b/hobsapp-dade2.appspot.com/o/hobsicon.png?alt=media&token=6aeb763d-b758-443c-ba2f-8b7ac083308f",
                                        "Welcome to EasyFix\nYour Home's Chief Caretaker"  ,
                                        it1.uid
                                    )
                                    makeToast("Worker")
                                }.addOnFailureListener {
                                    makeToast(it.message.toString())
                                }
                        }?.addOnFailureListener{
                            makeToast(it.message.toString())
                        }
                    }else if(designation == "User"){
                        val user = User(uid = uid,userName = username)
                        auth.currentUser?.let { it1 ->
                            db.collection("User")
                                .document(it1.uid)
                                .set(user)
                                .addOnSuccessListener {
                                    db.collection("LoginCheck")
                                        .document("LoginCheck")
                                        .update("users", FieldValue.arrayUnion(it1.uid))
                                    Utils.sendNotificationToUser(
                                        "https://firebasestorage.googleapis.com/v0/b/hobsapp-dade2.appspot.com/o/hobsicon.png?alt=media&token=6aeb763d-b758-443c-ba2f-8b7ac083308f",
                                        "Welcome to EasyFix\n" +
                                                "Your Home's Chief Caretaker"  ,
                                        it1.uid
                                    )
                                    makeToast("User")
                                }.addOnFailureListener {
                                    makeToast(it.message.toString())
                                }
                        }
                    }
                }.addOnFailureListener {
                    // if user not created
                    makeToast(it.message.toString())
                }
        }
    }
}