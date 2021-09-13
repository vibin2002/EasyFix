package com.killerinstinct.hobsapp.user.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.Utils
import com.killerinstinct.hobsapp.adapters.SearchAdapter
import com.killerinstinct.hobsapp.databinding.FragmentUserSearchBinding
import com.killerinstinct.hobsapp.model.Worker
import com.killerinstinct.hobsapp.viewmodel.UserMainViewModel

class UserSearchFragment : Fragment() {

    lateinit var binding: FragmentUserSearchBinding
    private val viewModel: UserMainViewModel by activityViewModels()
    private val workerList = mutableListOf<Worker>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nosearchresult = binding.emptyRv
        nosearchresult.visibility = View.GONE

        val searchAdapter = SearchAdapter(nosearchresult,requireContext(),workerList){
            val action = UserSearchFragmentDirections.actionUserNavigationSearchToUserShowProfileFragment(it)
            findNavController().navigate(action)
        }

        Utils.categories.forEach {
            val chip = layoutInflater.inflate(R.layout.categorychip, binding.searchCatchips, false) as Chip
            chip.text = it
            binding.searchCatchips.addView(chip)
        }

        binding.searchRv.apply {
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