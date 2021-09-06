package com.killerinstinct.hobsapp.worker.fragments

import android.app.ActionBar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.killerinstinct.hobsapp.databinding.FragmentWorkerProfileBinding
import com.killerinstinct.hobsapp.viewmodel.WorkerMainViewModel

class WorkerProfileFragment : Fragment() {

    lateinit var binding: FragmentWorkerProfileBinding
    private val viewModel: WorkerMainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkerProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.worker.observe(viewLifecycleOwner){
//            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
            val x = (FirebaseAuth.getInstance().currentUser!!.metadata!!.creationTimestamp)
            val y = Timestamp(x/1000,0)
            Toast.makeText(requireContext(),y.toDate().toString() , Toast.LENGTH_SHORT).show()
        }

    }

}