package com.killerinstinct.hobsapp.worker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.databinding.ActivityWorkerMainBinding
import com.killerinstinct.hobsapp.viewmodel.WorkerMainViewModel

class WorkerMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWorkerMainBinding
    private val viewModel: WorkerMainViewModel by viewModels()
    private var shown = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_HobsApp)
        binding = ActivityWorkerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getWorkerDetails()
        setupNav()
        viewModel.notification.observe(this){ notiList ->
            var count = 0
            notiList.forEach {
                if (!it.hasRead)
                    count++
            }
            if (count > 0) {
                binding.workerBtmNavbar.getOrCreateBadge(R.id.worker_navigation_notifications)
                    .number = count
            }
        }


    }

    private fun setupNav() {
        val navController = findNavController(R.id.nav_host_worker_fragment)
        binding.workerBtmNavbar.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.worker_navigation_home -> showBottomNav()
                R.id.worker_navigation_notifications -> showBottomNav()
                R.id.worker_navigation_profile -> showBottomNav()
                R.id.worker_navigation_search -> showBottomNav()
                else -> hideBottomNav()
            }
        }
    }

    private fun showBottomNav() {
        if (!shown) {
            binding.workerBtmNavbar.animate()
                .translationY(0f)
                .alpha(1.0f)
                .setListener(null)
            binding.workerBtmNavbar.visibility = View.VISIBLE
            shown = !shown
        }
    }

    private fun hideBottomNav() {
        if (shown) {
            binding.workerBtmNavbar.visibility = View.GONE
            binding.workerBtmNavbar.animate()
                .translationY(binding.workerBtmNavbar.height.toFloat())
                .alpha(1.0f)
                .setListener(null)
            shown = !shown
        }
    }
}