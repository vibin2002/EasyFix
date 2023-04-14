package com.killerinstinct.hobsapp.worker.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.killerinstinct.hobsapp.LoginActivity
import com.killerinstinct.hobsapp.PermissionUtils
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.databinding.FragmentWorkerHomeBinding
import com.killerinstinct.hobsapp.user.fragments.UserHomeFragment
import com.killerinstinct.hobsapp.viewmodel.WorkerMainViewModel


class WorkerHomeFragment : Fragment(), OnMapReadyCallback {

    lateinit var binding: FragmentWorkerHomeBinding
    lateinit var gMap: GoogleMap
    private val viewModel: WorkerMainViewModel by activityViewModels()
    private val TAG = "WorkerHomeFragment"

    companion object {
        private const val LOCATION_REQ_CODE = 10001;
        private const val TAG = "MapsActivity"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkerHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getWorkerDetails()
        viewModel.getAllJobs()
        viewModel.getWorkerRequests()

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

//        viewModel.jobs.observe(viewLifecycleOwner){ jobs ->
//            binding.wrkrTvJobscount.text = jobs.size.toString()
//            Log.d(TAG, "onViewCreated : ${jobs.size}")
//        }

//        viewModel.requests.observe(viewLifecycleOwner){ requests ->
//            binding.wrkrTvPendingcount.text = requests.size.toString()
//            Log.d(TAG, "onViewCreated : ${requests.size}")
//        }

        viewModel.worker.observe(viewLifecycleOwner){
            binding.name.text="Welcome back "+it.userName
            if (it.profilePic.length < 5){
                binding.homePropic.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.businessman
                    )
                )
            } else {
                Glide.with(requireContext())
                    .load(it.profilePic)
                    .into(binding.homePropic)
            }
        }

        binding.homePropic.setOnClickListener {
            PopupMenu(requireContext(),binding.homePropic).apply {
                inflate(R.menu.home_menu)
                setOnMenuItemClickListener {
                    if (it.itemId == R.id.logout){
                        viewModel.logoutWorker{
                            startActivity(Intent(requireActivity(), LoginActivity::class.java))
                            requireActivity().finish()
                        }
                    }
                    if (it.itemId == R.id.user_edit_profile_item){
                        val action = WorkerHomeFragmentDirections.actionWorkerNavigationHomeToWorkerNavigationProfile()
                        findNavController().navigate(action)
                    }
                    true
                }
                show()
            }
        }

//        binding.wrkrcardjobs.setOnClickListener {
//            val action = WorkerHomeFragmentDirections.actionWorkerNavigationHomeToWorkerJobsFragment()
//            findNavController().navigate(action)
//        }
//
//        binding.wrkrcardreq.setOnClickListener {
//            val action = WorkerHomeFragmentDirections.actionWorkerNavigationHomeToShowRequestsFragment()
//            findNavController().navigate(action)
//        }



    }

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
                gMap.addMarker(
                    MarkerOptions()
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
        try {
            val fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireContext())

            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                    if(location == null)
                        return@addOnSuccessListener
                    val loc = LatLng(location.latitude, location.longitude)
                    gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 12f))
                }
            }
        } catch (e: Exception){

        }

    }


}