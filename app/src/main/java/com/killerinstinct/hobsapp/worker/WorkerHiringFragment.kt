package com.killerinstinct.hobsapp.worker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.databinding.FragmentWorkerHiringBinding

class WorkerHiringFragment : Fragment() {

    lateinit var binding: FragmentWorkerHiringBinding
    private val args: WorkerHiringFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkerHiringBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.workerName.text = args.workerName
        binding.workerDesignation.text = args.workerCategory
        if (args.propic == "") {
            binding.workerPropic.setImageDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.ic_person
                )
            )
        }
        else{
            Glide.with(requireContext())
                .load(args.propic)
                .into(binding.workerPropic)
        }


    }


}