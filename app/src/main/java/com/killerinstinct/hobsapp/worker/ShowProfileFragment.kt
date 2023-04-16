package com.killerinstinct.hobsapp.worker

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.Utils
import com.killerinstinct.hobsapp.adapters.WorkerPostAdapter
import com.killerinstinct.hobsapp.databinding.FragmentShowProfileBinding
import com.killerinstinct.hobsapp.model.Job
import com.killerinstinct.hobsapp.model.Post
import com.killerinstinct.hobsapp.model.Worker
import com.killerinstinct.hobsapp.user.fragments.UserShowProfileFragmentDirections
import com.killerinstinct.hobsapp.viewmodel.WorkerMainViewModel

class ShowProfileFragment : Fragment() {

    lateinit var binding: FragmentShowProfileBinding
    private val args: ShowProfileFragmentArgs by navArgs()
    private val userUid = FirebaseAuth.getInstance().currentUser?.uid
    private var worker: Worker? = null
    private val viewModel: WorkerMainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.btnHire.setOnClickListener
        binding.nestedScrollViewWorkershowprofile.visibility = View.INVISIBLE
        binding.progbarLoad.visibility = View.VISIBLE

        binding.clickView.setOnClickListener {
            if (binding.ghostLayout.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(
                    binding.ghostLayout,
                    AutoTransition()
                )
                binding.ghostLayout.setVisibility(View.GONE)
                binding.arrow.setImageResource(R.drawable.arrow_down)
                binding.info.setText("More information")
            } else {
                TransitionManager.beginDelayedTransition(
                    binding.ghostLayout,
                    AutoTransition()
                )
                binding.ghostLayout.setVisibility(View.VISIBLE)
                binding.arrow.setImageResource(R.drawable.arrow_up)
                binding.info.setText("Less information")
            }
        }

        Utils.getSpecificWorker(args.workerId){
            binding.apply {
                nestedScrollViewWorkershowprofile.visibility = View.VISIBLE
                progbarLoad.visibility = View.GONE
                worker = it
                tutorName.text = it.userName
                tutEmail.text = it.email
                tutPhonenum.text = it.phoneNumber
                tutStatus.text = it.status
                val lastseen =  Utils.getLastSeenString(it.lastSeen)
                tutCategory.text = it.category.toString().removePrefix("[").removeSuffix("]")
//                wrkrReviewCount.text = it.ratersCount+" reviews"
//                wrkrRating.text = it.rating
//                var address: Address? = null
//                val geocoder = Geocoder(context)
//                val addresses = geocoder.getFromLocation(
//                    it.location.latitude,
//                    it.location.longitude,
//                    1
//                )
//                if(addresses.isNotEmpty()){
//                    address = addresses[0]
//                    val locality = address.locality ?: ""
//                    val sublocality = address.subLocality ?: ""
//                    tutLocation.text = "$sublocality $locality"
//                }
                if (it.profilePic != "") {
                    Log.d("Glidy", "onViewCreated: podA")
                    Glide.with(requireContext())
                        .load(it.profilePic)
                        .into(binding.tutorProfile)
                }else{
                    binding.tutorProfile.setImageDrawable(
                        AppCompatResources.getDrawable(
                            requireContext(),
                            R.drawable.businessman
                        )
                    )
                }
            }
        }

        viewModel.getSpecificWorkerPosts(args.workerId){
            if(it.size==0)
            {
                binding.emptyRv.visibility=View.VISIBLE
                binding.showprofileRv.visibility=View.GONE
            }
            else {
                binding.emptyRv.visibility = View.GONE
                setUpRecyclerView(it)
            }
        }

        binding.allReviewsTv.setOnClickListener {
            val action = ShowProfileFragmentDirections.actionShowProfileFragmentToReviewFragment2(
                args.workerId
            )
            findNavController().navigate(action)
        }



    }

    private fun setUpRecyclerView(list: List<Post>){
        binding.showprofileRv.adapter = WorkerPostAdapter(requireContext(),list){
            val extras = FragmentNavigatorExtras(it.second to "postpreview")
            val timestamp = it.first.date.toDate().toString().split(" ").toMutableList()
            timestamp.removeLast()
            timestamp.removeLast()
            val action = ShowProfileFragmentDirections.actionShowProfileFragmentToViewSinglePostFragment2(
                it.first.url,
                it.first.description,
                timestamp.toList().toString().removeSuffix("]").removePrefix("["),
                false
            )
            findNavController().navigate(action,extras)
        }
        binding.showprofileRv.layoutManager = GridLayoutManager(requireContext(),3)
    }

}