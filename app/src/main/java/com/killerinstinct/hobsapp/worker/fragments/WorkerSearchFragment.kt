package com.killerinstinct.hobsapp.worker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
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
    private val workerList = mutableListOf<Worker>()

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

        viewModel.getAllWorkers()

        val searchAdapter = SearchAdapter(requireContext(),workerList)
        binding.searchRv.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.allWorkers.observe(viewLifecycleOwner){
            it.forEach { worker ->
                workerList.add(worker)
            }
            searchAdapter.notifyDataSetChanged()
        }

        binding.wrkrsearchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchAdapter.filter.filter(newText)
                return false
            }

        })


    }




}