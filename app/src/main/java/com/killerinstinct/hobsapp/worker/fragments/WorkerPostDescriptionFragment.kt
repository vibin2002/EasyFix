package com.killerinstinct.hobsapp.worker.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
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
        binding.chooseImage.setOnClickListener {
            fetchImage()
        }

        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Uploading photo")

        binding.uploadPost.setOnClickListener {
            progressDialog.show()
            val worker = viewModel.worker.value
            val description = binding.descriptionOfPost.text.toString()
            if (worker == null || imageUri == null){
                Toast.makeText(requireContext(), "Error uploading post", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
                return@setOnClickListener
            }
            if (description.isNullOrEmpty()){
                progressDialog.dismiss()
                Toast.makeText(requireContext(), "Provide proper description", Toast.LENGTH_SHORT).show()
            }
            viewModel.uploadPost(
                imageUri!!,
                description,
                worker.uid
            ){ isUploaded ->
                if (isUploaded){
                    progressDialog.dismiss()
                    Snackbar.make(requireView(), "Post uploaded successfully", Snackbar.LENGTH_LONG).show()
                    viewModel.fetchWorkerPosts(worker.uid)
                    findNavController().navigateUp()
                } else {
                    progressDialog.dismiss()
                    Snackbar.make(requireView(), "Post not uploaded", Snackbar.LENGTH_LONG).show()
                }
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
            binding.ivImagepreview.visibility = View.GONE
            binding.tvImagepreview.visibility = View.GONE
            imageUri = data.data
            binding.postImage.setImageURI(imageUri)
        }
    }


}