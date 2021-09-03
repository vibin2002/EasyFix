package com.killerinstinct.hobsapp

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.viewModels
import com.killerinstinct.hobsapp.databinding.ActivityLoginBinding
import com.killerinstinct.hobsapp.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    private lateinit var progressDialog: ProgressDialog
    val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.dontHavAcc.setOnClickListener {
            startActivity(Intent(this,SignupActivity::class.java))
        }

        progressDialog = ProgressDialog(this)

        binding.loginBtn.setOnClickListener{
            if (TextUtils.isEmpty(binding.emailInLogin.toString()) || TextUtils.isEmpty(binding.passwordInLogin.toString()))
            {
                Toast.makeText(this, "please fill the field!", Toast.LENGTH_SHORT).show()
            }
            else
            {
                progressDialog!!.setMessage("Logging in Please wait...")

                progressDialog!!.show()

                loginViewModel.loginUser(
                    binding.emailInLogin.text.toString(),
                    binding.passwordInLogin.text.toString()
                ) { isValid ->
                    if (isValid == "Worker") {
                        progressDialog!!.dismiss()
                        Toast.makeText(this, "login Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                    } else if (isValid == "User") {
                        progressDialog!!.dismiss()
                        Toast.makeText(this, "login Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                    } else if (isValid == "Failure") {
                        progressDialog!!.dismiss()
                        Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show()
                    }

                }
            }

        }


    }
}