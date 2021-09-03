package com.killerinstinct.hobsapp

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.killerinstinct.hobsapp.databinding.ActivitySignupBinding
import com.killerinstinct.hobsapp.viewmodel.SignupViewModel

class SignupActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    var desig = "NULL"
    private lateinit var progressDialog: ProgressDialog
    lateinit var binding: ActivitySignupBinding
    private val signUpViewModel: SignupViewModel by viewModels()
    val context: Context = this


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this)


        binding.spBtn.setOnClickListener {

            //if the fields are empty
            if (TextUtils.isEmpty(binding.spUsername.toString()) || TextUtils.isEmpty(binding.spEmail.toString())
                || TextUtils.isEmpty(binding.spPassword.toString())) {
                Toast.makeText(this, "please fill the field!", Toast.LENGTH_SHORT).show()
            } else {
                progressDialog!!.setMessage("Signing up Please wait")

                progressDialog!!.show()


                signUpViewModel.signUpWithCustomAuth(
                    binding.spUsername.text.toString(),
                    binding.spEmail.text.toString(),
                    binding.spPassword.text.toString(),
                    getDesignation()
                ) { signUpState ->
                    when (signUpState) {
                        "Worker" -> {
                            progressDialog!!.dismiss()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                        "User" -> {
                            progressDialog!!.dismiss()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                        else -> {
                            progressDialog!!.dismiss()
                            Toast.makeText(this, signUpState, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        binding.alreadyHavAcc.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }



    }
    private fun getDesignation(): String {
        val id = binding.radioGrp.checkedRadioButtonId
        return findViewById<RadioButton>(id).text.toString().trim()
    }
}