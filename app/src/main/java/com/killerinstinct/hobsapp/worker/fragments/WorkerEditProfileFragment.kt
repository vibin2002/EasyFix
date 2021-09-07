package com.killerinstinct.hobsapp.worker.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.Utils
import com.killerinstinct.hobsapp.databinding.FragmentWorkerEditProfileBinding
import com.killerinstinct.hobsapp.viewmodel.WorkerMainViewModel

class WorkerEditProfileFragment : Fragment() {

    lateinit var binding: FragmentWorkerEditProfileBinding
    private val viewModel: WorkerMainViewModel by activityViewModels()
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkerEditProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.worker.observe(viewLifecycleOwner){
            binding.wdPhonenumber.setText(it.phoneNumber)
            binding.wdExperience.setText(it.experience)
            binding.wdLocation.setText(it.location.toString())
            binding.wdMinwage.setText(it.minWage)
            if (it.profilePic != "") {
                Glide.with(requireContext())
                    .load(it.profilePic)
                    .into(binding.ivPropic)
            }else{
                Glide.with(requireContext())
                    .load("https://firebasestorage.googleapis.com/v0/b/hobsapp-dade2.appspot.com/o/3t7eeIK8BdTtKjcm0RTewMk1xPh1%2FprofilePicture?alt=media&token=c3b704f0-12bb-425e-b95d-0deeb7dc3550")
                    .into(binding.ivPropic)
            }

            Utils.categories.forEach { category ->
                val chip = layoutInflater.inflate(R.layout.categorychip, binding.categoryCg, false) as Chip
                chip.text = category
                if (category in it.category){
                    chip.isChecked = true
                }
                binding.categoryCg.addView(chip)
            }
        }

        binding.addPropic.setOnClickListener {
            fetchImage()
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
            viewModel.updateWorkerInfo(
                binding.wdPhonenumber.text.toString(),
                binding.wdExperience.text.toString(),
                binding.wdMinwage.text.toString(),
                cats,
                imageUri ?: Uri.parse(""),
            ){
                findNavController().navigateUp()
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
    }

}