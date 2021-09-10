package com.killerinstinct.hobsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.model.Notification
import de.hdodenhof.circleimageview.CircleImageView

class NotificationsAdapter(
    val list: List<Notification>
): RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.notification_card,parent,false)
        )
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val timestamp = list[position].timestamp.toDate().toString().split(" ").toMutableList()
        timestamp.removeLast()
        timestamp.removeLast()
        holder.description.text = list[position].description
        holder.timestamp.text = timestamp.toList().toString().removeSuffix("]").removePrefix("[")
    }

    override fun getItemCount() = list.size

    inner class NotificationViewHolder(view: View): RecyclerView.ViewHolder(view){
        val image = itemView.findViewById<CircleImageView>(R.id.notification_image)
        val description = itemView.findViewById<TextView>(R.id.notification_description)
        val timestamp = itemView.findViewById<TextView>(R.id.notification_timestamp)
    }

}