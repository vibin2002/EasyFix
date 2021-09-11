package com.killerinstinct.hobsapp.worker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.killerinstinct.hobsapp.Utils
import com.killerinstinct.hobsapp.databinding.FragmentShowProfileBinding
import com.killerinstinct.hobsapp.model.Job
import com.killerinstinct.hobsapp.model.Worker

class ShowProfileFragment : Fragment() {

    lateinit var binding: FragmentShowProfileBinding
    private val args: ShowProfileFragmentArgs by navArgs()
    private val userUid = FirebaseAuth.getInstance().currentUser?.uid
    private var worker: Worker? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.btnHire.setOnClickListener

        Utils.getSpecificWorker(args.workerId){
            binding.apply {
                worker = it
                tutorName.text = it.userName
                tutEmail.text = it.email
                tutPhonenum.text = it.phoneNumber
                tutMinwage.text = it.minWage
                tutExperience.text = it.experience
                tutCategory.text = it.category.toString().removePrefix("[").removeSuffix("]")
                wrkrReviewCount.text = it.ratersCount
                wrkrRating.text = it.rating
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
//



    }

}