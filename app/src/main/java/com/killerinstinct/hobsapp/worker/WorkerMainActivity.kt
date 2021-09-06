package com.killerinstinct.hobsapp.worker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_HobsApp)
        binding = ActivityWorkerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_worker_fragment)
        binding.workerBtmNavbar.setupWithNavController(navController)

        viewModel.getWorkerDetails()


    }
}