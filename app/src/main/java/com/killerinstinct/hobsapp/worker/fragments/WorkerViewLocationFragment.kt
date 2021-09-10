package com.killerinstinct.hobsapp.worker.fragments

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.databinding.FragmentWorkerViewLocationBinding

class WorkerViewLocationFragment : Fragment(), OnMapReadyCallback {

    lateinit var binding: FragmentWorkerViewLocationBinding
    lateinit var gMap: GoogleMap
    private val args: WorkerViewLocationFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkerViewLocationBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.viewlocmap) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        val userLoc = LatLng(args.latitude.toDouble(),args.longitude.toDouble())
        val workerloc = LatLng(args.wrkrLat.toDouble(),args.wrkrLong.toDouble())
        Toast.makeText(requireContext(), "$workerloc", Toast.LENGTH_SHORT).show()
        gMap.addMarker(MarkerOptions().position(userLoc).title(args.name))
        gMap.addMarker(MarkerOptions().position(workerloc).title("Your position"))
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLoc,12f))

        binding.confirmloc.setOnClickListener {
            val gmmIntentUri =
                Uri.parse("google.navigation:q=${args.latitude},${args.longitude}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)


        }


    }
}