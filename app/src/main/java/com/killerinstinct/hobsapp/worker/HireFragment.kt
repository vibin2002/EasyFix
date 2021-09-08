package com.killerinstinct.hobsapp.worker

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.Utils
import com.killerinstinct.hobsapp.databinding.FragmentHireBinding

class HireFragment : Fragment() {

    lateinit var binding: FragmentHireBinding
    val args: HireFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHireBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("Thanos", "onViewCreated: ${args.fromId}")


    }


}