package com.killerinstinct.hobsapp.worker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.killerinstinct.hobsapp.databinding.FragmentWorkerProfileBinding

class WorkerProfileFragment : Fragment() {

    lateinit var binding: FragmentWorkerProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkerProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

}