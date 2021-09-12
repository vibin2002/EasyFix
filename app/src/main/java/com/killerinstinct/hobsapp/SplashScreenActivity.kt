package com.killerinstinct.hobsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.killerinstinct.hobsapp.model.LoginCheck
import com.killerinstinct.hobsapp.user.UserMainActivity
import com.killerinstinct.hobsapp.worker.WorkerMainActivity

class SplashScreenActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            db.collection("LoginCheck")
                .document("LoginCheck")
                .get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        val loginCheck = it.result?.toObject(LoginCheck::class.java)
                        when {
                            loginCheck!!.users.contains(user.uid) -> {
                                startActivity(Intent(this, UserMainActivity::class.java))
                                finish()
                            }
                            loginCheck.workers.contains(user.uid) -> {
                                startActivity(Intent(this, WorkerMainActivity::class.java))
                                finish()
                            }
                            else -> {
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }
                        }
                    } else {
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                }
        }

    }
}