package com.killerinstinct.hobsapp.worker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.databinding.ActivityWorkerMainBinding

class WorkerMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWorkerMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_HobsApp)
        binding = ActivityWorkerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_worker_fragment)
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.worker_navigation_home,
//                R.id.worker_navigation_search,
//                R.id.worker_navigation_chat,
//                R.id.worker_navigation_notifications,
//                R.id.worker_navigation_profile
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.workerBtmNavbar.setupWithNavController(navController)

    }
}