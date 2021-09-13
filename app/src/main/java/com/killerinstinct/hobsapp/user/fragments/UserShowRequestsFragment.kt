package com.killerinstinct.hobsapp.user.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
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

        binding.tvNorequests.visibility = View.GONE
        binding.norequestsiv.visibility = View.GONE
        binding.progbarreq.visibility = View.VISIBLE

        viewModel.getUserRequests()
        viewModel.requests.observe(viewLifecycleOwner){
            binding.progbarreq.visibility = View.GONE
            setUpRecyclerView(it)
        }

    }

    private fun setUpRecyclerView(requests: List<Request>) {
        if (requests.isEmpty()){
            binding.tvNorequests.visibility = View.VISIBLE
            binding.norequestsiv.visibility = View.VISIBLE
        }
        binding.showRequestsRv.adapter = UserShowRequestsAdapter(requests,requireContext())
        binding.showRequestsRv.layoutManager = LinearLayoutManager(requireContext())
    }

}