package com.killerinstinct.hobsapp.worker.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.databinding.FragmentWorkerChatBinding
import com.killerinstinct.hobsapp.databinding.FragmentWorkerUpdateDetailsBinding

class WorkerUpdateDetailsFragment : Fragment() {

    lateinit var binding: FragmentWorkerUpdateDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkerUpdateDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

}