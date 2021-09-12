package com.killerinstinct.hobsapp.worker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.Utils
import com.killerinstinct.hobsapp.adapters.NotificationsAdapter
import com.killerinstinct.hobsapp.databinding.FragmentWorkerNotificationBinding
import com.killerinstinct.hobsapp.model.Notification
import com.killerinstinct.hobsapp.viewmodel.WorkerMainViewModel

class WorkerNotificationFragment : Fragment() {

    lateinit var binding: FragmentWorkerNotificationBinding
    private val viewModel: WorkerMainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkerNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navbar = requireActivity().findViewById<BottomNavigationView>(R.id.worker_btm_navbar)
        try {
            navbar.getOrCreateBadge(R.id.worker_navigation_notifications).isVisible = false
        } catch (e: Exception){

        }

        viewModel.notification.observe(viewLifecycleOwner){
            setupRecyclerView(it)
        }
        viewModel.notifyIds.observe(viewLifecycleOwner){
            val workerId = viewModel.worker.value?.uid ?: return@observe
            Utils.checkReadNotificationsWorker(workerId,it)
        }

    }

    private fun setupRecyclerView(notificationList: List<Notification>) {
        binding.wrkrNotifyRv.adapter = NotificationsAdapter(requireContext(),notificationList)
        binding.wrkrNotifyRv.layoutManager = LinearLayoutManager(requireContext())
    }
}