package com.killerinstinct.hobsapp.user.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.databinding.FragmentUserHomeBinding
import com.killerinstinct.hobsapp.databinding.FragmentUserNotificationBinding

class UserNotificationFragment : Fragment() {

    lateinit var binding: FragmentUserNotificationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navbar = requireActivity().findViewById<BottomNavigationView>(R.id.user_btm_navbar)
        navbar.getOrCreateBadge(R.id.user_navigation_notifications).number = 1

    }

}