package com.killerinstinct.hobsapp.worker.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.GeoPoint
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.Utils
import com.killerinstinct.hobsapp.databinding.FragmentWorkerDetailsBinding
import com.killerinstinct.hobsapp.viewmodel.WorkerMainViewModel
import com.killerinstinct.hobsapp.worker.WorkerMainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkerDetailsFragment : Fragment() {

    lateinit var binding: FragmentWorkerDetailsBinding
    private val viewModel: WorkerMainViewModel by activityViewModels()
    private val args: WorkerDetailsFragmentArgs by navArgs()
    var imageUri: Uri? = null

    companion object{
        val LOCATION_REQ_CODE = 1001
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkerDetailsBinding.inflate(layoutInflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.wdLocation.setText(args.city)
        val progressDialog = ProgressDialog(requireContext())

        binding.wdChooseLocBtn.setOnClickListener {
            val action = WorkerDetailsFragmentDirections.actionWorkerDetailsFragmentToWorkerDetailLocationFragment()
            findNavController().navigate(action)
        }

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

            cats.add(binding.otherCategory.text.toString())

            CoroutineScope(Dispatchers.IO).launch {
                if (args.latitude == 0.0f && args.longitude == 0.0f) {
                    Toast.makeText(requireContext(),"Choose a valid location", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                progressDialog.setMessage("Updating info...")
                viewModel.updateWorkerInfo(
                  GeoPoint(args.latitude.toDouble(),args.longitude.toDouble()),
                    binding.wdPhonenumber.text.toString(),
                    binding.wdExperience.text.toString(),
                    binding.wdMinwage.text.toString(),
                    cats,
                    imageUri
                ){
                    if (it) {
                        Toast.makeText(requireContext(), "Details updated", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(Intent(requireActivity(), WorkerMainActivity::class.java))
                        requireActivity().finish()
                        progressDialog.dismiss()
                    }
                    else{
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT)
                            .show()
                        progressDialog.dismiss()
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