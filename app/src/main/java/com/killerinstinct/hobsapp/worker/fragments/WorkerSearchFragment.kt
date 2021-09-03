package com.killerinstinct.hobsapp.worker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.killerinstinct.hobsapp.databinding.FragmentWorkerSearchBinding


class WorkerSearchFragment : Fragment() {

    lateinit var binding: FragmentWorkerSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkerSearchBinding.inflate(inflater,container,false)
        return binding.root
    }

}