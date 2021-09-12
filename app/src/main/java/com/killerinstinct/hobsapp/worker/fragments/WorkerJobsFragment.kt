package com.killerinstinct.hobsapp.worker.fragments

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.Utils
import com.killerinstinct.hobsapp.adapters.WorkerJobsAdapter
import com.killerinstinct.hobsapp.databinding.FragmentWorkerJobsBinding
import com.killerinstinct.hobsapp.model.Job
import com.killerinstinct.hobsapp.viewmodel.WorkerMainViewModel

class WorkerJobsFragment : Fragment() {

    lateinit var binding: FragmentWorkerJobsBinding
    private val viewModel: WorkerMainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkerJobsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progbarWorkerJobs.visibility = View.VISIBLE

        viewModel.getAllJobs()
        viewModel.jobs.observe(viewLifecycleOwner){
            binding.progbarWorkerJobs.visibility = View.GONE
            setUpRecyclerView(it)
        }

    }

    private fun setUpRecyclerView(list: List<Job>){
        binding.workerJobsrv.adapter = WorkerJobsAdapter(viewModel.worker.value!!.location,findNavController(),requireContext(),list){
            viewModel.markJobAsCompleted(it.second){
                if (it){
                    findNavController().navigateUp()
                    Snackbar.make(requireView(),"Marked as done",Snackbar.LENGTH_SHORT).show()
                } else {
                    findNavController().navigateUp()
                    Snackbar.make(requireView(),"Error",Snackbar.LENGTH_SHORT).show()
                }

            }
            val worker = viewModel.worker.value ?: return@WorkerJobsAdapter
            Utils.sendNotificationToUser(
                worker.profilePic,
                "${worker.userName.capitalize()} has completed the job..Don't forget to give a review! ",
                it.first
            )
        }
    }

}