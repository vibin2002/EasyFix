package com.killerinstinct.hobsapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.model.Worker
import de.hdodenhof.circleimageview.CircleImageView

class SearchAdapter(
    private val context: Context,
    private val workerList: List<Worker>
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.search_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.apply {
            Glide.with(context)
                .load(workerList[position].profilePic)
                .into(propic)
            name.text = workerList[position].userName
            designation.text = workerList[position].category.toString()
        }
    }

    override fun getItemCount() = workerList.size

    inner class SearchViewHolder(
        private val itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val propic = itemView.findViewById<CircleImageView>(R.id.search_propic)
        val name = itemView.findViewById<TextView>(R.id.search_name)
        val designation = itemView.findViewById<TextView>(R.id.search_designation)
    }
}