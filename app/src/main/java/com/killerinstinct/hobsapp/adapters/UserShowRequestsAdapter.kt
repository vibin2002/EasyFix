package com.killerinstinct.hobsapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.model.Request
import de.hdodenhof.circleimageview.CircleImageView

class UserShowRequestsAdapter(
    private val requests: List<Request>,
    private val context: Context
): RecyclerView.Adapter<UserShowRequestsAdapter.ShowRequestsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowRequestsViewHolder {
        return ShowRequestsViewHolder(
            LayoutInflater.from(context).inflate(R.layout.user_request_card,parent,false)
        )
    }

    override fun onBindViewHolder(holder: ShowRequestsViewHolder, position: Int) {
        holder.apply {
            name.text = requests[position].to
            description.text = requests[position].description
            //TODO load propic
        }
    }

    override fun getItemCount() = requests.size

    inner class ShowRequestsViewHolder(view: View):
        RecyclerView.ViewHolder(view){
        val imageView = itemView.findViewById<CircleImageView>(R.id.request_image)
        val name = itemView.findViewById<TextView>(R.id.request_name)
        val description = itemView.findViewById<TextView>(R.id.request_description)
    }

}