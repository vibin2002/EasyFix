package com.killerinstinct.hobsapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.model.Worker

class WorkerPostAdapter(
    private val context: Context,
    private var list: List<String>
) : RecyclerView.Adapter<WorkerPostAdapter.WorkerPostViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WorkerPostAdapter.WorkerPostViewHolder {
        return WorkerPostViewHolder(
            LayoutInflater.from(context).inflate(R.layout.post_card,parent,false)
        )
    }

    override fun onBindViewHolder(holder: WorkerPostAdapter.WorkerPostViewHolder, position: Int) {
        Glide.with(context)
            .load(list[position])
            .into(holder.photoView)
    }

    override fun getItemCount() = list.size

    inner class WorkerPostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val photoView: ImageView = itemView.findViewById<ImageView>(R.id.postImage)
    }

}