package com.killerinstinct.hobsapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.killerinstinct.hobsapp.databinding.ActivityWorkerDetailsBinding
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.GeoPoint
import com.killerinstinct.hobsapp.viewmodel.WorkerDetailsViewModel
import com.killerinstinct.hobsapp.worker.WorkerMainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class WorkerDetails : AppCompatActivity() {

    lateinit var binding: ActivityWorkerDetailsBinding
    private val viewModel: WorkerDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkerDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}