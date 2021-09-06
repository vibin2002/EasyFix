package com.killerinstinct.hobsapp.worker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.killerinstinct.hobsapp.adapters.SearchAdapter
import com.killerinstinct.hobsapp.databinding.FragmentWorkerSearchBinding
import com.killerinstinct.hobsapp.model.Worker
import com.killerinstinct.hobsapp.viewmodel.WorkerMainViewModel


class WorkerSearchFragment : Fragment() {

    lateinit var binding: FragmentWorkerSearchBinding
    private val viewModel: WorkerMainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkerSearchBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView(listOf())

        viewModel.worker.observe(viewLifecycleOwner){
            val list = listOf(it,it,it,it,it)
            setupRecyclerView(list)
        }

    }

    private fun setupRecyclerView(list: List<Worker>){
        binding.searchRv.apply {
            adapter = SearchAdapter(requireContext(),list)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }



}