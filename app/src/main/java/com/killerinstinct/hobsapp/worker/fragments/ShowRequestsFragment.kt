package com.killerinstinct.hobsapp.worker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.GeoPoint
import com.killerinstinct.hobsapp.Utils
import com.killerinstinct.hobsapp.adapters.WorkerShowRequestsAdapter
import com.killerinstinct.hobsapp.databinding.FragmentShowRequestsBinding
import com.killerinstinct.hobsapp.model.Job
import com.killerinstinct.hobsapp.model.Request
import com.killerinstinct.hobsapp.viewmodel.WorkerMainViewModel

class ShowRequestsFragment : Fragment() {
    lateinit var binding: FragmentShowRequestsBinding
    private val viewModel: WorkerMainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowRequestsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progbarr.visibility = View.VISIBLE

        viewModel.getWorkerRequests()
        val workerLoc = viewModel.worker.value?.location
        viewModel.requests.observe(viewLifecycleOwner){
            binding.progbarr.visibility = View.GONE
            if (workerLoc == null)
                return@observe
            if(it.size==0)
            {
                binding.emptyRv.visibility=View.VISIBLE
                binding.showReqRv.visibility=View.GONE
            }
            else {
                binding.emptyRv.visibility = View.GONE
                setUpRecyclerView(it,workerLoc)
            }
        }


    }

    private fun setUpRecyclerView(requests: List<Request>,workerLoc: GeoPoint) {
        binding.showReqRv.adapter = WorkerShowRequestsAdapter(
            workerLoc,findNavController(),
            requests,
            requireContext()
        ){ decision ->
            if (decision.second){   // If request accepted
                val job = Job(
                    decision.first.requestId,
                    decision.first.fromName,
                    decision.first.from,
                    decision.first.to,
                    decision.first.description,
                    decision.first.reqDate,
                    decision.first.reqTime,
                    decision.first.location,
                    decision.first.contact,
                )
                viewModel.addJob(job)
                Utils.sendNotificationToUser(
                    viewModel.worker.value?.profilePic ?: "",
                    "${viewModel.worker.value!!.userName} has accepted your service request" ,
                    decision.first.from
                )
            } else {                // If request declined
                viewModel.deleteRequest(
                    decision.first.requestId,
                    decision.first.from,
                    decision.first.to,
                )
                Utils.sendNotificationToUser(
                    viewModel.worker.value?.profilePic ?: "",
                    "${viewModel.worker.value!!.userName} has declined your service request" ,
                    decision.first.from,
                )
            }
            findNavController().navigateUp()
            viewModel.getWorkerRequests()
        }
        binding.showReqRv.layoutManager = LinearLayoutManager(requireContext())
    }


}