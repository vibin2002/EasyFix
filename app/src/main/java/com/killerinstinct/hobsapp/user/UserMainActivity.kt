package com.killerinstinct.hobsapp.user

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.databinding.ActivityUserMainBinding
import com.killerinstinct.hobsapp.viewmodel.UserMainViewModel

class UserMainActivity : AppCompatActivity() {

    lateinit var binding: ActivityUserMainBinding
    private val viewModel: UserMainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_user_fragment)
        binding.userBtmNavbar.setupWithNavController(navController)

        viewModel.getAllWorkers()


    }
}