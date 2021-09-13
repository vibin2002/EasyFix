package com.killerinstinct.hobsapp.user.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.Utils
import com.killerinstinct.hobsapp.adapters.WorkerPostAdapter
import com.killerinstinct.hobsapp.databinding.FragmentUserShowProfileBinding
import com.killerinstinct.hobsapp.model.Post
import com.killerinstinct.hobsapp.model.Worker
import com.killerinstinct.hobsapp.viewmodel.UserMainViewModel

class UserShowProfileFragment : Fragment() {

    lateinit var binding: FragmentUserShowProfileBinding
    private val args: UserShowProfileFragmentArgs by navArgs()
    private var worker: Worker? = null
    private val viewModel: UserMainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserShowProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.call.setOnClickListener {
            var i= Intent(Intent.ACTION_DIAL)
            i.setData(Uri.parse("tel:"+(binding.tutPhonenum.text)))
            startActivity(i)
        }
        binding.mailIntent.setOnClickListener {
            var mailIntent=Intent(Intent.ACTION_VIEW,Uri.parse("mailto:"+binding.tutEmail.text))
            startActivity(mailIntent)
        }

        binding.nestedScrollView.visibility = View.INVISIBLE
        binding.progbar.visibility = View.VISIBLE

        Utils.getSpecificWorker(args.workerId){
            worker = it
            binding.apply {
                nestedScrollView.visibility = View.VISIBLE
                progbar.visibility = View.GONE
                tutorName.text = it.userName
                tutEmail.text = it.email
                tutPhonenum.text = it.phoneNumber
                tutMinwage.text = it.minWage+"/day"
                tutExperience.text = it.experience+" years"
                tutCategory.text = it.category.toString().removePrefix("[").removeSuffix("]")
                usrReviewCount.text = "${it.ratersCount} reviewers"
                usrRating.text = it.rating
                if (it.profilePic != "") {
                    Log.d("Glidy", "onViewCreated: podA")
                    Glide.with(requireContext())
                        .load(it.profilePic)
                        .into(binding.tutorProfile)
                }else{
                    Log.d("Glidy", "onViewCreated: here")
                    binding.tutorProfile.setImageDrawable(
                        AppCompatResources.getDrawable(
                            requireContext(),
                            R.drawable.ic_person
                        )
                    )
                }
            }
        }
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

        binding.btnHire.setOnClickListener {
            if (worker == null)
                return@setOnClickListener
            val action = UserShowProfileFragmentDirections.actionUserShowProfileFragmentToUserHiringFragment(
                worker!!.userName,
                worker!!.category.toString().removePrefix("[").removeSuffix("]"),
                worker!!.profilePic,
                args.workerId
            )
            findNavController().navigate(action)
        }

        viewModel.getSpecificWorkerPosts(args.workerId){
            setUpRecyclerView(it)
        }

        binding.allReviewsTv.setOnClickListener {
            val action = UserShowProfileFragmentDirections.actionUserShowProfileFragmentToReviewFragment(
                args.workerId
            )
            findNavController().navigate(action)
        }


    }

    fun setUpRecyclerView(list: List<Post>){
        binding.showprofileRv.adapter = WorkerPostAdapter(requireContext(),list){
            val extras = FragmentNavigatorExtras(it.second to "postpreview")
            val timestamp = it.first.date.toDate().toString().split(" ").toMutableList()
            timestamp.removeLast()
            timestamp.removeLast()
            val action = UserShowProfileFragmentDirections.actionUserShowProfileFragmentToViewSinglePostFragment(
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