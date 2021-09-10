package com.killerinstinct.hobsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.model.Job

class JobsAdapter(
    val list: List<Job>
): RecyclerView.Adapter<JobsAdapter.JobViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        return JobViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_job_card,parent,false)
        )
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        holder.workername.text = list[position].toId   //TODO( id to name )
        holder.time.text = list[position].reqTime
        holder.date.text = list[position].reqDate
        holder.description.text = list[position].description
    }

    override fun getItemCount() = list.size

    inner class JobViewHolder(view: View): RecyclerView.ViewHolder(view){
        val workername = itemView.findViewById<TextView>(R.id.job_name)
        val time = itemView.findViewById<TextView>(R.id.jobtime)
        val date = itemView.findViewById<TextView>(R.id.jobdate)
        val description = itemView.findViewById<TextView>(R.id.job_description)
    }

}