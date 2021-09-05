package com.killerinstinct.hobsapp.adapters

import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.killerinstinct.hobsapp.model.Worker

class WorkerPostAdapter(
    private val navController: NavController,
    private var list: List<Worker>
) : RecyclerView.Adapter<WorkerPostAdapter.WorkerPostViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WorkerPostAdapter.WorkerPostViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: WorkerPostAdapter.WorkerPostViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    inner class WorkerPostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}