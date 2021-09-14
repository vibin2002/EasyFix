package com.killerinstinct.hobsapp.user

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint
import com.killerinstinct.hobsapp.databinding.FragmentUserDetailBinding
import com.killerinstinct.hobsapp.viewmodel.UserDetailViewModel

class UserDetailFragment : Fragment() {

    lateinit var binding: FragmentUserDetailBinding
    private val viewModel: UserDetailViewModel by activityViewModels()
    private val args: UserDetailFragmentArgs by navArgs()
    private var imageUri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etLocation.setText(args.city)

        binding.chooseLocation.setOnClickListener {
            val action = UserDetailFragmentDirections.actionUserDetailFragmentToDetailLocationFragment()
            findNavController().navigate(action)
        }

        binding.addPropic.setOnClickListener {
            fetchImage()
        }

        binding.btnSubmit.setOnClickListener {
            viewModel.updateUserInfo(
                imageUri,
                binding.wdPhonenumber.text.toString(),
                args.city,
                GeoPoint(args.latitude.toDouble(),args.latitude.toDouble())
            ){
                if (it) {
                    requireContext().startActivity(
                        Intent(
                            requireActivity(),
                            UserMainActivity::class.java
                        )
                    )
                    requireActivity().finish()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error updating details..try logging in",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }

    private fun fetchImage() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(intent, 100)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && data != null) {
            imageUri = data.data
            binding.ivPropic.setImageURI(imageUri)
        }
    }


}