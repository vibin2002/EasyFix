package com.killerinstinct.hobsapp.user.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.Utils
import com.killerinstinct.hobsapp.adapters.UserJobsAdapter
import com.killerinstinct.hobsapp.databinding.FragmentUserJobsBinding
import com.killerinstinct.hobsapp.model.Job
import com.killerinstinct.hobsapp.user.ReviewDialog
import com.killerinstinct.hobsapp.viewmodel.UserMainViewModel

class UserJobsFragment : Fragment() {

    lateinit var binding: FragmentUserJobsBinding
    private val viewModel: UserMainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserJobsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progbarLoadUserJob.visibility = View.VISIBLE

        viewModel.jobs.observe(viewLifecycleOwner){
            binding.progbarLoadUserJob.visibility = View.GONE
            if(it.size==0)
            {
                binding.emptyRv.visibility=View.VISIBLE
            }
            else {
                binding.emptyRv.visibility = View.GONE
                setUpRecyclerView(it)
            }
        }

    }

    private fun setUpRecyclerView(list: List<Job>){
        binding.userJobRv.adapter = UserJobsAdapter(requireContext(),list){
            val user = viewModel.user.value ?: return@UserJobsAdapter
            val dialog = ReviewDialog(
                user.uid,
                user.userName,
                user.profile,
                it
            ){ isPosted ->
                if (isPosted){
                    Snackbar.make(requireView(), "Thanks for reviewing", Snackbar.LENGTH_LONG).show()
                    viewModel.getAllWorkers()
                    Utils.sendNotificationToWorker(
                        user.profile,
                        "${user.userName} has given review on your work",
                        it
                    )
                } else {
                    Snackbar.make(requireView(), "Network Error", Snackbar.LENGTH_LONG).show()
                }
            }
            dialog.show(parentFragmentManager,"Example")
        }
        binding.userJobRv.layoutManager = LinearLayoutManager(requireContext())
    }

}