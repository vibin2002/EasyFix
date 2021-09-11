package com.killerinstinct.hobsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.util.Util
import com.killerinstinct.hobsapp.adapters.ReviewAdapter
import com.killerinstinct.hobsapp.databinding.FragmentReviewBinding
import com.killerinstinct.hobsapp.model.Review

class ReviewFragment : Fragment() {

    lateinit var binding: FragmentReviewBinding
    private val args: ReviewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Utils.getSpecificUserReviews(args.workerId){
            setUpRecyclerView(it)
        }

    }

    fun setUpRecyclerView(list: List<Review>) {
        binding.reviewRv.adapter = ReviewAdapter(requireContext(),list)
        binding.reviewRv.layoutManager = LinearLayoutManager(requireContext())
    }

}