package com.killerinstinct.hobsapp

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.util.Util
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.killerinstinct.hobsapp.databinding.FragmentChooseLocationBinding
import com.killerinstinct.hobsapp.databinding.FragmentViewSinglePostBinding

class ViewSinglePostFragment : Fragment() {

    lateinit var binding: FragmentViewSinglePostBinding
    private val args: ViewSinglePostFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewSinglePostBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.explode)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.isWorker){
            binding.deletepost.visibility = View.VISIBLE
        } else {
            binding.deletepost.visibility = View.GONE
        }

        binding.deletepost.setOnClickListener {
            Utils.deleteWorkerPost(
                FirebaseAuth.getInstance().currentUser!!.uid,
                args.posturl,
            ){
                Snackbar.make(requireView(),"Post deleted",Snackbar.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }

        binding.postdescription.text = args.description
        binding.postdate.text = args.datetime
        Log.d("PostPreview", "onViewCreated: ${args.posturl}")
        Glide.with(requireContext())
            .load(args.posturl)
            .into(binding.postpreview)

    }
}