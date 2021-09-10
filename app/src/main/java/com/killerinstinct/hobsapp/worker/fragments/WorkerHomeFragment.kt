package com.killerinstinct.hobsapp.worker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.killerinstinct.hobsapp.databinding.FragmentWorkerHomeBinding
import com.killerinstinct.hobsapp.viewmodel.WorkerMainViewModel


class WorkerHomeFragment : Fragment() {

    lateinit var binding: FragmentWorkerHomeBinding
    private val viewModel: WorkerMainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkerHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getWorkerDetails()
        binding.reqs.setOnClickListener {
            val action = WorkerHomeFragmentDirections.actionWorkerNavigationHomeToShowRequestsFragment()
            findNavController().navigate(action)
        }



    }
}