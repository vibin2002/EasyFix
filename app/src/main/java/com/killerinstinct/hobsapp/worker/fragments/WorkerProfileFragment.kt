package com.killerinstinct.hobsapp.worker.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.Utils
import com.killerinstinct.hobsapp.adapters.WorkerPostAdapter
import com.killerinstinct.hobsapp.databinding.FragmentWorkerProfileBinding
import com.killerinstinct.hobsapp.model.Post
import com.killerinstinct.hobsapp.viewmodel.WorkerMainViewModel
import com.killerinstinct.hobsapp.worker.ShowProfileFragmentDirections

class WorkerProfileFragment : Fragment() {

    lateinit var binding: FragmentWorkerProfileBinding
    private val TAG = "WorkerProfile"
    private val viewModel: WorkerMainViewModel by activityViewModels()
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setWorkerData()
        binding = FragmentWorkerProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addPosts.setOnClickListener {
            findNavController().navigate(R.id.action_worker_navigation_profile_to_workerPostDescriptionFragment)
        }

        binding.editProfile.setOnClickListener {
            findNavController().navigate(R.id.action_worker_navigation_profile_to_workerEditProfileFragment)
        }

        viewModel.posts.observe(viewLifecycleOwner){
            if(it.size==0)
            {
                binding.emptyRv.visibility=View.VISIBLE
                binding.postsRecyclerView.visibility=View.GONE
            }
            else {
                binding.emptyRv.visibility = View.GONE
                setUpRecyclerView(it)
            }
        }

    }

    private fun setUpRecyclerView(list: List<Post>){
        binding.postsRecyclerView.adapter = WorkerPostAdapter(requireContext(),list){
            val extras = FragmentNavigatorExtras(it.second to "postpreview")
            val timestamp = it.first.date.toDate().toString().split(" ").toMutableList()
            timestamp.removeLast()
            timestamp.removeLast()
            val action = WorkerProfileFragmentDirections.actionWorkerNavigationProfileToViewSinglePostFragment2(
                it.first.url,
                it.first.description,
                timestamp.toList().toString().removeSuffix("]").removePrefix("["),
                true
            )
            findNavController().navigate(action,extras)
        }
        binding.postsRecyclerView.layoutManager = GridLayoutManager(requireContext(),3)
    }


    private fun setWorkerData() {
        val viewModel= ViewModelProvider(requireActivity()).get(WorkerMainViewModel::class.java)
        viewModel.getWorkerDetails()
        viewModel.worker.observe(viewLifecycleOwner) {
            binding.email.text = it.email
            binding.phno.text = "+91 "+it.phoneNumber
            binding.rate.text = it.minWage+"/day"
            binding.experience.text = it.experience+" years"
            binding.workerName.text = it.userName
            binding.rating.text = it.rating
            binding.reviewCnt.text = it.ratersCount+" reviews"

            binding.location.text=Utils.getLocationAddress(11.004556,76.961632,requireContext())
            Log.d(TAG,Utils.getLocationAddress(11.004556,76.961632,requireContext()))
            binding.category.text = it.category.toString().removePrefix("[").removeSuffix("]")
            if (it.profilePic == "")
                binding.workerProfile.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_person
                    )
                )
            else {
                Glide.with(requireContext())
                    .load(it.profilePic)
                    .into(binding.workerProfile)
            }

        }
    }




}