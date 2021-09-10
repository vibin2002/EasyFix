package com.killerinstinct.hobsapp.user.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.Utils
import com.killerinstinct.hobsapp.adapters.NotificationsAdapter
import com.killerinstinct.hobsapp.databinding.FragmentUserHomeBinding
import com.killerinstinct.hobsapp.databinding.FragmentUserNotificationBinding
import com.killerinstinct.hobsapp.model.Notification
import com.killerinstinct.hobsapp.viewmodel.UserMainViewModel

class UserNotificationFragment : Fragment() {

    lateinit var binding: FragmentUserNotificationBinding
    private val viewModel: UserMainViewModel by activityViewModels()

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
        try {
            navbar.getOrCreateBadge(R.id.user_navigation_notifications).isVisible = false
        } catch (e: Exception){

        }

        viewModel.notification.observe(viewLifecycleOwner){
            setupRecyclerView(it)
            Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
            Utils.checkReadNotifications()
        }

    }

    private fun setupRecyclerView(notificationList: List<Notification>) {
        binding.notifyRv.adapter = NotificationsAdapter(notificationList)
        binding.notifyRv.layoutManager = LinearLayoutManager(requireContext())
    }

}