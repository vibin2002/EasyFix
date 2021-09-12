package com.killerinstinct.hobsapp.user.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.killerinstinct.hobsapp.*
import com.killerinstinct.hobsapp.adapters.UserJobsAdapter
import com.killerinstinct.hobsapp.databinding.FragmentUserHomeBinding
import com.killerinstinct.hobsapp.model.Job
import com.killerinstinct.hobsapp.user.ReviewDialog
import com.killerinstinct.hobsapp.viewmodel.UserMainViewModel

class UserHomeFragment : Fragment(),OnMapReadyCallback {

    lateinit var binding: FragmentUserHomeBinding
    lateinit var gMap: GoogleMap
    private val viewModel: UserMainViewModel by activityViewModels()

    companion object {
        private const val LOCATION_REQ_CODE = 10001;
        private const val TAG = "MapsActivity"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        Utils.categories.forEach {
            val chip = layoutInflater.inflate(R.layout.categorychip, binding.catchips, false) as Chip
            chip.text = it
            binding.catchips.addView(chip)
        }

        binding.progbar.visibility = View.VISIBLE

        viewModel.jobs.observe(viewLifecycleOwner){
            binding.progbar.visibility = View.GONE
            if (it.isEmpty()){
                binding.upcoming.visibility = View.GONE
//                binding.userJobsRv.visibility = View.GONE
            }
//            setUpRecyclerView(it)
        }

        viewModel.user.observe(viewLifecycleOwner){
            if (it.profile.length < 5){
                binding.homePropic.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_person
                    )
                )
            } else {
                Glide.with(requireContext())
                    .load(it.profile)
                    .into(binding.homePropic)
            }
        }


        binding.homePropic.setOnClickListener {
            PopupMenu(requireContext(),binding.homePropic).apply {
                inflate(R.menu.home_menu)
                setOnMenuItemClickListener {
                    if (it.itemId == R.id.logout){
                        viewModel.logoutUser(){
                            startActivity(Intent(requireActivity(),LoginActivity::class.java))
                            requireActivity().finish()
                        }
                    }
                    true
                }
                show()
            }
        }


        binding.showRequests.setOnClickListener {
            val action = UserHomeFragmentDirections.actionUserNavigationHomeToUserShowRequestsFragment()
            findNavController().navigate(action)
        }

    }

//    private fun setUpRecyclerView(list: List<Job>){
//        binding.userJobsRv.adapter = UserJobsAdapter(requireContext(),list){
//            val user = viewModel.user.value ?: return@UserJobsAdapter
//            val dialog = ReviewDialog(
//                user.uid,
//                user.userName,
//                user.profile,
//                it
//            ){ isPosted ->
//                if (isPosted){
//                    Snackbar.make(requireView(), "Thanks for reviewing", Snackbar.LENGTH_LONG).show()
//                    Utils.sendNotificationToWorker(
//                        user.profile,
//                        "${user.userName} has given review on your work",
//                        it
//                    )
//                } else {
//                    Snackbar.make(requireView(), "Network Error", Snackbar.LENGTH_LONG).show()
//                }
//            }
//            dialog.show(parentFragmentManager,"Example")
//        }
//        binding.userJobsRv.layoutManager = LinearLayoutManager(requireContext())
//    }


    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        checkPermissions()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
        }
        viewModel.allWorkers.observe(viewLifecycleOwner){ workers ->
            if (workers == null)
                return@observe
            workers.forEach {
                val loc = LatLng(it.location.latitude,it.location.longitude)
                gMap.addMarker(MarkerOptions()
                    .position(loc)
                    .title(it.userName)
                    .snippet(it.category.toString().removeSuffix("]").removePrefix("["))
                )
            }

        }
    }

    private fun checkPermissions(){
        when {
            PermissionUtils.isAccessFineLocationGranted(requireContext())
                    && PermissionUtils.isAccessCoarseLocationGranted(requireContext())
            -> {
                when {
                    PermissionUtils.isLocationEnabled(requireContext()) -> {
                        getLastLocation()
                    }
                    else -> {
                        PermissionUtils.showGPSNotEnabledDialog(requireContext())
                    }
                }
            }
            else -> {
                PermissionUtils.requestAccessFineLocationPermission(
                    requireActivity(),
                    LOCATION_REQ_CODE
                )
                PermissionUtils.requestAccessCoarseLocationPermission(
                    requireActivity(),
                    LOCATION_REQ_CODE
                )
            }
        }
    }

    private fun getLastLocation(){
        Toast.makeText(requireContext(),"getLastLocation", Toast.LENGTH_SHORT).show()
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                val loc = LatLng(location.latitude,location.longitude)
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc,12f))
            }
        }

    }

}