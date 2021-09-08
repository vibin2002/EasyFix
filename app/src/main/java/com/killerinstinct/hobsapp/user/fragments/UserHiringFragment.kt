package com.killerinstinct.hobsapp.user.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.databinding.FragmentUserHiringBinding

class UserHiringFragment : Fragment() {
    lateinit var binding: FragmentUserHiringBinding
    private val args: UserHiringFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserHiringBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.workerName.text = args.workerName
        binding.workerDesignation.text = args.workerDesignation
        if (args.propic == "") {
            binding.workerPropic.setImageDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.ic_person
                )
            )
        }
        else{
            Glide.with(requireContext())
                .load(args.propic)
                .into(binding.workerPropic)
        }

    }
}