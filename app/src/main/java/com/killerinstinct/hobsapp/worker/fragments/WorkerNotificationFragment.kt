package com.killerinstinct.hobsapp.worker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.killerinstinct.hobsapp.databinding.FragmentWorkerNotificationBinding

class WorkerNotificationFragment : Fragment() {

    lateinit var binding: FragmentWorkerNotificationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkerNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }
}