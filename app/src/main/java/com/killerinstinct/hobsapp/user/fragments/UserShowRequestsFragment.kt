package com.killerinstinct.hobsapp.user.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.adapters.UserShowRequestsAdapter
import com.killerinstinct.hobsapp.databinding.FragmentUserShowRequestsBinding
import com.killerinstinct.hobsapp.model.Request
import com.killerinstinct.hobsapp.viewmodel.UserMainViewModel

class UserShowRequestsFragment : Fragment() {

    lateinit var binding: FragmentUserShowRequestsBinding
    private val viewModel: UserMainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserShowRequestsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUserRequests()
        viewModel.requests.observe(viewLifecycleOwner){
            setUpRecyclerView(it)
        }

    }

    private fun setUpRecyclerView(requests: List<Request>) {
        binding.showRequestsRv.adapter = UserShowRequestsAdapter(requests,requireContext())
        binding.showRequestsRv.layoutManager = LinearLayoutManager(requireContext())
    }

}