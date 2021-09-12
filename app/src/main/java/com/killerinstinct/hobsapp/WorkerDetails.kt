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
    private var imageUri: Uri? = null
    var chosenLocation: LatLng? = null

    companion object {
        private const val LOCATION_REQ_CODE = 10001
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkerDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Utils.categories.forEach {
            val chip = layoutInflater.inflate(R.layout.categorychip, binding.categoryCg, false) as Chip
            chip.text = it
            binding.categoryCg.addView(chip)
        }

        binding.btnOk.setOnClickListener {
            val list = binding.categoryCg.children.toList().filter {
                (it as Chip).isChecked
            }
            val cats = mutableListOf<String>()
            for (chip in list) {
                val str = (chip as Chip).text
                cats.add(str.toString())
            }

            CoroutineScope(Dispatchers.IO).launch {
                if (chosenLocation == null)
                    return@launch
                viewModel.updateWorkerInfo(
                  GeoPoint(chosenLocation!!.latitude,chosenLocation!!.longitude),
                    binding.wdPhonenumber.text.toString(),
                    binding.wdExperience.text.toString(),
                    binding.wdMinwage.text.toString(),
                    cats,
                    imageUri
                ){
                    if (it == "Picuploaded") {
                        Toast.makeText(this@WorkerDetails, "Details updated", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(Intent(this@WorkerDetails, WorkerMainActivity::class.java))
                        finish()
                    }
                    else{
                        Toast.makeText(this@WorkerDetails, "Error", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            }
        }

        binding.addPropic.setOnClickListener {
            fetchImage()
        }



    }

    private fun getLocationPermission(){
        when {
            PermissionUtils.isAccessFineLocationGranted(this)
                    && PermissionUtils.isAccessCoarseLocationGranted(this)
            -> {
                when {
                    PermissionUtils.isLocationEnabled(this) -> {
                        getLastLocation()
                    }
                    else -> {
                        PermissionUtils.showGPSNotEnabledDialog(this)
                    }
                }
            }
            else -> {
                PermissionUtils.requestAccessFineLocationPermission(
                    this,
                    LOCATION_REQ_CODE
                )
                PermissionUtils.requestAccessCoarseLocationPermission(
                    this,
                    LOCATION_REQ_CODE
                )
                getLocationPermission()
            }
        }
    }

    private fun getLastLocation(){
        Toast.makeText(this,"getLastLocation",Toast.LENGTH_SHORT).show()
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                    val loc = LatLng(location.latitude, location.longitude)
                    chosenLocation = loc
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Make sure that you have given location permission and given location access", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchImage() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(intent, 100)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && data != null) {
            imageUri = data.data
            binding.ivPropic.setImageURI(imageUri)
        }
        getLocationPermission()
    }
}