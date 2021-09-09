package com.killerinstinct.hobsapp.user

import android.os.Bundle
import android.view.View
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
    private var shown = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_user_fragment)
        binding.userBtmNavbar.setupWithNavController(navController)

        viewModel.getAllWorkers()
        viewModel.fetchUserDetail()
        setupNav()
    }

    private fun setupNav() {
        val navController = findNavController(R.id.nav_host_user_fragment)
        binding.userBtmNavbar.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.user_navigation_home -> showBottomNav()
                R.id.user_navigation_search -> showBottomNav()
                R.id.user_navigation_chat -> showBottomNav()
                R.id.user_navigation_notifications -> showBottomNav()
                else -> hideBottomNav()
            }
        }
    }

    private fun showBottomNav() {
        if (!shown) {
            binding.userBtmNavbar.animate()
                .translationY(0f)
                .alpha(1.0f)
                .setListener(null)
            binding.userBtmNavbar.visibility = View.VISIBLE
            shown = !shown
        }
    }

    private fun hideBottomNav() {
        if (shown) {
            binding.userBtmNavbar.animate()
                .translationY(binding.userBtmNavbar.height.toFloat())
                .alpha(1.0f)
                .setListener(null)
            shown = !shown
            binding.userBtmNavbar.visibility = View.GONE
        }
    }

}