package com.killerinstinct.hobsapp.worker.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.killerinstinct.hobsapp.LoginActivity
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.databinding.FragmentWorkerHomeBinding
import com.killerinstinct.hobsapp.viewmodel.WorkerMainViewModel


class WorkerHomeFragment : Fragment() {

    lateinit var binding: FragmentWorkerHomeBinding
    private val viewModel: WorkerMainViewModel by activityViewModels()
    private val TAG = "WorkerHomeFragment"

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
        viewModel.getAllJobs()
        viewModel.getWorkerRequests()

        viewModel.jobs.observe(viewLifecycleOwner){ jobs ->
            binding.wrkrTvJobscount.text = jobs.size.toString()
            Log.d(TAG, "onViewCreated : ${jobs.size}")
        }

        viewModel.requests.observe(viewLifecycleOwner){ requests ->
            binding.wrkrTvPendingcount.text = requests.size.toString()
            Log.d(TAG, "onViewCreated : ${requests.size}")
        }

        viewModel.worker.observe(viewLifecycleOwner){
            if (it.profilePic.length < 5){
                binding.homePropic.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_person
                    )
                )
            } else {
                Glide.with(requireContext())
                    .load(it.profilePic)
                    .into(binding.homePropic)
            }
        }

        binding.homePropic.setOnClickListener {
            PopupMenu(requireContext(),binding.homePropic).apply {
                inflate(R.menu.home_menu)
                setOnMenuItemClickListener {
                    if (it.itemId == R.id.logout){
                        viewModel.logoutWorker{
                            startActivity(Intent(requireActivity(), LoginActivity::class.java))
                            requireActivity().finish()
                        }
                    }
                    if (it.itemId == R.id.user_edit_profile_item){
                        val action = WorkerHomeFragmentDirections.actionWorkerNavigationHomeToWorkerNavigationProfile()
                        findNavController().navigate(action)
                    }
                    true
                }
                show()
            }
        }

        binding.wrkrcardjobs.setOnClickListener {
            val action = WorkerHomeFragmentDirections.actionWorkerNavigationHomeToWorkerJobsFragment()
            findNavController().navigate(action)
        }

        binding.wrkrcardreq.setOnClickListener {
            val action = WorkerHomeFragmentDirections.actionWorkerNavigationHomeToShowRequestsFragment()
            findNavController().navigate(action)
        }



    }
}