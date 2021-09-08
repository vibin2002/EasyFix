package com.killerinstinct.hobsapp.user.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.Utils
import com.killerinstinct.hobsapp.databinding.FragmentUserShowProfileBinding
import com.killerinstinct.hobsapp.model.Worker

class UserShowProfileFragment : Fragment() {

    lateinit var binding: FragmentUserShowProfileBinding
    private val args: UserShowProfileFragmentArgs by navArgs()
    private var worker: Worker? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserShowProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nestedScrollView.visibility = View.INVISIBLE
        binding.progbar.visibility = View.VISIBLE

        Utils.getSpecificWorker(args.workerId){
            worker = it
            binding.apply {
                nestedScrollView.visibility = View.VISIBLE
                progbar.visibility = View.GONE
                tutorName.text = it.userName
                tutEmail.text = it.email
                tutPhonenum.text = it.phoneNumber
                tutMinwage.text = it.minWage
                tutExperience.text = it.experience
                tutCategory.text = it.category.toString().removePrefix("[").removeSuffix("]")
                if (it.profilePic != "") {
                    Log.d("Glidy", "onViewCreated: podA")
                    Glide.with(requireContext())
                        .load(it.profilePic)
                        .into(binding.tutorProfile)
                }else{
                    Log.d("Glidy", "onViewCreated: here")
                    Glide.with(requireContext())
                        .load("https://firebasestorage.googleapis.com/v0/b/hobsapp-dade2.appspot.com/o/3t7eeIK8BdTtKjcm0RTewMk1xPh1%2FprofilePicture?alt=media&token=c3b704f0-12bb-425e-b95d-0deeb7dc3550")
                        .into(binding.tutorProfile)
                }
            }
        }

        binding.btnHire.setOnClickListener {
            if (worker == null)
                return@setOnClickListener
            val action = UserShowProfileFragmentDirections.actionUserShowProfileFragmentToUserHiringFragment(
                worker!!.userName,
                worker!!.category.toString().removePrefix("[").removeSuffix("]"),
                worker!!.profilePic,
                args.workerId
            )
            findNavController().navigate(action)
        }

    }

}