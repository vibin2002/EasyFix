package com.killerinstinct.hobsapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import com.killerinstinct.hobsapp.databinding.ActivityWorkerDetailsBinding
import com.google.android.material.chip.Chip
import com.killerinstinct.hobsapp.viewmodel.WorkerDetailsViewModel
import com.killerinstinct.hobsapp.worker.WorkerMainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class WorkerDetails : AppCompatActivity() {

    lateinit var binding: ActivityWorkerDetailsBinding
    private val viewModel: WorkerDetailsViewModel by viewModels()
    private var imageUri: Uri? = null

    private val categories = listOf(
        "Plumber",
        "Painter",
        "Fitter",
        "Electrician",
        "Gardner",
        "Interior decorator",
        "Mason",
        "Smart appliances installer",
        "Automobile repair",
        "Two-wheeler repair"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkerDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        categories.forEach {
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
                viewModel.updateWorkerInfo(
                    binding.wdLocation.text.toString(),
                    binding.wdPhonenumber.text.toString(),
                    binding.wdExperience.text.toString(),
                    binding.wdMinwage.text.toString(),
                    cats,
                    imageUri
                ){
                    if (it) {
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
    }
}