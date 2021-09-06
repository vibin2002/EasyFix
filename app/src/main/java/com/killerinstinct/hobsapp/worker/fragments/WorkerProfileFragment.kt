package com.killerinstinct.hobsapp.worker.fragments

import android.app.ActionBar
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.databinding.FragmentWorkerProfileBinding
import com.killerinstinct.hobsapp.viewmodel.WorkerMainViewModel
import java.io.IOException
import java.lang.StringBuilder
import java.util.*

class WorkerProfileFragment : Fragment() {

    lateinit var binding: FragmentWorkerProfileBinding
    private val viewModel: WorkerMainViewModel by viewModels()
    private val TAG = "WorkerProfile"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkerProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getWorkerDetails()

        viewModel.worker.observe(viewLifecycleOwner) {
//            val x = (FirebaseAuth.getInstance().currentUser!!.metadata!!.creationTimestamp)
//            val y = Timestamp(x/1000,0)
//            Toast.makeText(requireContext(),y.toDate().toString() , Toast.LENGTH_SHORT).show()
            binding.email.text = it.email
            binding.phno.text = it.phoneNumber
            binding.rate.text = it.minWage
            binding.experience.text = it.experience
            binding.workerName.text = it.userName
            binding.rating.text = it.rating
            binding.reviewCnt.text = it.ratersCount

//            val geocoder = Geocoder(requireContext(), Locale.getDefault())
//            var addresses = geocoder.getFromLocation(it.location.latitude, it.location.longitude, 1)
//            binding.location.text = addresses[0].locality+","+addresses[0].countryName
//            Log.d(TAG,"$addresses")


            binding.category.text = it.category.toString().removePrefix("[").removeSuffix("]")
            if (it.profilePic == "")
                binding.workerProfile.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_person
                    )
                )
            else {
                Glide.with(requireContext())
                    .load(it.profilePic)
                    .into(binding.workerProfile)
            }
        }

    }

}