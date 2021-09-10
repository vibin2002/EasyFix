package com.killerinstinct.hobsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.killerinstinct.hobsapp.R
import de.hdodenhof.circleimageview.CircleImageView

class NotificationsAdapter(

): RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.notification_card,parent,false)
        )
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    inner class NotificationViewHolder(view: View): RecyclerView.ViewHolder(view){
        val image = itemView.findViewById<CircleImageView>(R.id.notification_image)
        val description = itemView.findViewById<CircleImageView>(R.id.notification_description)
        val timestamp = itemView.findViewById<CircleImageView>(R.id.notification_timestamp)
    }

}