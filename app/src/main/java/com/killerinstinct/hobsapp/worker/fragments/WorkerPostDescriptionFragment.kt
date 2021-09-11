package com.killerinstinct.hobsapp.worker.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.databinding.FragmentWorkerPostDescriptionBinding
import com.killerinstinct.hobsapp.databinding.FragmentWorkerProfileBinding
import com.killerinstinct.hobsapp.viewmodel.WorkerMainViewModel

class WorkerPostDescriptionFragment : Fragment() {

    lateinit var binding: FragmentWorkerPostDescriptionBinding
    private val viewModel: WorkerMainViewModel by activityViewModels()
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkerPostDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.postImage.setOnClickListener {
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
            binding.postImage.setImageURI(imageUri)
        }
    }


}