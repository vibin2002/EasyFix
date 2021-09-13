package com.killerinstinct.hobsapp.worker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.killerinstinct.hobsapp.adapters.SearchAdapter
import com.killerinstinct.hobsapp.databinding.FragmentWorkerSearchBinding
import com.killerinstinct.hobsapp.model.Worker
import com.killerinstinct.hobsapp.viewmodel.WorkerMainViewModel

class WorkerSearchFragment : Fragment() {

    lateinit var binding: FragmentWorkerSearchBinding
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
        val viewModel=ViewModelProvider(requireActivity()).get(WorkerMainViewModel::class.java)
        viewModel.getAllWorkers()


        val nosearchresult = binding.emptyRv
        nosearchresult.visibility = View.GONE

        val searchAdapter = SearchAdapter(nosearchresult,requireContext(),workerList){
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            val action = WorkerSearchFragmentDirections.actionWorkerNavigationSearchToShowProfileFragment(it)
            findNavController().navigate(action)
        }
         binding.wrkrSearchRv.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.allWorkers.observe(viewLifecycleOwner){
            if (it.isEmpty()){
                nosearchresult.visibility = View.VISIBLE
            }
            workerList.clear()
            workerList.addAll(it)
            searchAdapter.notifyDataSetChanged()
        }

        binding.searchCatchips.setOnCheckedChangeListener { _, checkedId ->
            val chip = view.findViewById<Chip>(checkedId)
            if(chip == null){
                searchAdapter.filter.filter("")
                return@setOnCheckedChangeListener
            }
            searchAdapter.filter.filter((chip as Chip).text)
        }
        binding.wrkrsearchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchAdapter.filter.filter(newText)
                return true
            }

        })


    }




}