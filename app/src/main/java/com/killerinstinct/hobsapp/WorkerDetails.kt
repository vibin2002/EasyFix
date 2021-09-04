package com.killerinstinct.hobsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.children
import com.killerinstinct.hobsapp.databinding.ActivityWorkerDetailsBinding
import com.google.android.material.chip.Chip
import com.killerinstinct.hobsapp.viewmodel.WorkerDetailsViewModel


class WorkerDetails : AppCompatActivity() {

    lateinit var binding: ActivityWorkerDetailsBinding
    private val viewModel: WorkerDetailsViewModel by viewModels()

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
            val list = binding.categoryCg.
                    children.toList().filter {
                (it as Chip).isChecked
            }
            val cats = mutableListOf<String>()
            for (chip in list){
                val str = (chip as Chip).text
                cats.add(str.toString())
            }

            val state = viewModel.updateWorkerInfo(
                binding.wdLocation.text.toString(),
                binding.wdPhonenumber.text.toString(),
                binding.wdExperience.text.toString(),
                binding.wdMinwage.text.toString(),
                cats
            )

//            Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
        }


    }
}