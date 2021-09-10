package com.killerinstinct.hobsapp.user.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.databinding.FragmentUserHiringBinding
import com.killerinstinct.hobsapp.viewmodel.UserMainViewModel
import java.util.*

class UserHiringFragment : Fragment() {
    lateinit var binding: FragmentUserHiringBinding
    private val args: UserHiringFragmentArgs by navArgs()
    private val viewModel: UserMainViewModel by viewModels()
    private var endDateSelected: String? = null
    private var time:String?=null
    private val TAG = "UserHiringFragment"
    private var userid: String? = null
    private var contact: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserHiringBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchUserDetail()
        viewModel.user.observe(viewLifecycleOwner){
            userid = it.uid
            contact = it.phoneNumber
        }
        binding.chooseLocation.setOnClickListener {
            val action = UserHiringFragmentDirections.actionUserHiringFragmentToChooseLocationFragment(
                args.workerName,
                args.workerDesignation,
                args.propic,
                args.workerId
            )
            findNavController().navigate(action)
        }

        binding.workerName.text = args.workerName
        binding.workerDesignation.text = args.workerDesignation
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

        binding.btnSendServicereq.setOnClickListener {
            if (!binding.checkBox.isChecked){
                Toast.makeText(requireContext(), "You must agree to share your details to send a service request", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (endDateSelected == null || time == null || userid == null || contact == null) {
                Toast.makeText(requireContext(), "Fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.sendWorkRequest(
                viewModel.user.value!!.userName ,
                userid!!,
                args.workerId,
                binding.description.text.toString(),
                GeoPoint(args.latitude.toDouble(),args.longitude.toDouble()),
                endDateSelected!!,
                time!!,
                contact!!
            ){ isSent ->
                if (isSent){
                    Snackbar.make(requireView(),"Request sent",Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(requireView(),"Request not sent",Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        binding.datePicker.setOnClickListener {
            datePick()
        }

        binding.timePicker.setOnClickListener {
            timePick()
        }

    }

    private fun datePick(){
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val date = calendar[Calendar.DATE]
        val listener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            endDateSelected = dayOfMonth.toString() + "-" + (month + 1) + "-" + year
            binding.datePicker.setText(endDateSelected)
        }
        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            listener,
            year, month, date
        )
        datePickerDialog.show()
    }

    private fun timePick(){
        val calendar=Calendar.getInstance()
        val hour=calendar[Calendar.HOUR]
        val minute=calendar[Calendar.MINUTE]
        val ampm = if( hour<12) "AM" else "PM"
        val timePickerDialog = TimePickerDialog(
            requireActivity(),
            {view,hours,minutes->
                time = "$hours:$minutes $ampm"
                binding.timePicker.setText(time)
            },
            hour,minute, DateFormat.is24HourFormat(requireContext())
        )
        timePickerDialog.show()
    }

    override fun onStart() {
        super.onStart()
        binding.etLocation.setText(args.city)
    }
}