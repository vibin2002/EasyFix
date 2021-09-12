package com.killerinstinct.hobsapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.killerinstinct.hobsapp.databinding.ActivityLoginBinding
import com.killerinstinct.hobsapp.user.UserMainActivity
import com.killerinstinct.hobsapp.viewmodel.LoginViewModel
import com.killerinstinct.hobsapp.worker.WorkerMainActivity

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    private lateinit var progressDialog: ProgressDialog
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_HobsApp)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.dontHavAcc.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        progressDialog = ProgressDialog(this)

        binding.loginBtn.setOnClickListener{
            if ((binding.emailInLogin.toString().trim().length>0 ) && (binding.passwordInLogin.toString().trim().length>0 ))
            {
                progressDialog.setMessage("Logging in Please wait...")

                progressDialog.show()

                loginViewModel.loginUser(
                    binding.emailInLogin.text.toString(),
                    binding.passwordInLogin.text.toString()
                ) { isValid ->
                    if (isValid == "Worker") {
                        progressDialog.dismiss()
                        Toast.makeText(this, "login Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, WorkerMainActivity::class.java))
                        finish()
                    } else if (isValid == "User") {
                        progressDialog.dismiss()
                        Toast.makeText(this, "login Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, UserMainActivity::class.java))
                        finish()
                    } else if (isValid == "Failure") {
                        progressDialog.dismiss()
                        Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show()
                    }

                }

            }
            else
            {
                Toast.makeText(this, "please fill the field!", Toast.LENGTH_SHORT).show()
            }

        }


    }
}