package com.killerinstinct.hobsapp.worker.fragments

import android.app.ActionBar
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.Utils
import com.killerinstinct.hobsapp.databinding.FragmentWorkerProfileBinding
import com.killerinstinct.hobsapp.viewmodel.WorkerMainViewModel
import java.io.IOException
import java.lang.StringBuilder
import java.util.*

class WorkerProfileFragment : Fragment() {

    lateinit var binding: FragmentWorkerProfileBinding
    private val TAG = "WorkerProfile"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setWorkerData()
        binding = FragmentWorkerProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.call.setOnClickListener {
            var i=Intent(Intent.ACTION_DIAL)
            i.setData(Uri.parse("tel:"+(binding.phno.text)))
            startActivity(i)
        }
        binding.mailIntent.setOnClickListener {
             var mailIntent=Intent(Intent.ACTION_VIEW,Uri.parse("mailto:"+binding.email.text))
            startActivity(mailIntent)
        }

        binding.editProfile.setOnClickListener {
            findNavController().navigate(R.id.action_worker_navigation_profile_to_workerEditProfileFragment)
        }

    }

    private fun setWorkerData() {
        val viewModel= ViewModelProvider(requireActivity()).get(WorkerMainViewModel::class.java)
        viewModel.getWorkerDetails()
        viewModel.worker.observe(viewLifecycleOwner) {
            binding.email.text = it.email
            binding.phno.text = "+91 "+it.phoneNumber
            binding.rate.text = it.minWage+"/month"
            binding.experience.text = it.experience+" years"
            binding.workerName.text = it.userName
            binding.rating.text = it.rating
            binding.reviewCnt.text = it.ratersCount

            binding.location.text=Utils.getLocationAddress(11.004556,76.961632,requireContext())
            Log.d(TAG,Utils.getLocationAddress(11.004556,76.961632,requireContext()))
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