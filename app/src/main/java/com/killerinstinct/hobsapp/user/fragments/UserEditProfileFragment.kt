package com.killerinstinct.hobsapp.user.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.databinding.FragmentUserEditProfileBinding
import com.killerinstinct.hobsapp.viewmodel.UserMainViewModel

class UserEditProfileFragment : Fragment() {

    lateinit var binding: FragmentUserEditProfileBinding
    private var imageUri: Uri? = null
    private val viewModel: UserMainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserEditProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addPropic.setOnClickListener{
            fetchImage()
        }
        val user = viewModel.user.value!!
        binding.username.setText(user.userName)
        binding.wdPhonenumber.setText(user.phoneNumber)
        binding.city.setText(user.city)
        if (user.profile.length < 5){
            binding.ivPropic.setImageDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.ic_person
                )
            )
        } else {
            Glide.with(requireContext())
                .load(user.profile)
                .into(binding.ivPropic)
        }

        binding.btnSubmit.setOnClickListener {
            val progressDialog = ProgressDialog(requireContext())
            progressDialog.setMessage("Updating info please wait...")
            progressDialog.show()
            viewModel.updateUserData(
                binding.username.text.toString(),
                binding.wdPhonenumber.text.toString(),
                binding.city.text.toString(),
                imageUri
            ){
                progressDialog.dismiss()
                findNavController().navigateUp()
                viewModel.fetchUserDetail()
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