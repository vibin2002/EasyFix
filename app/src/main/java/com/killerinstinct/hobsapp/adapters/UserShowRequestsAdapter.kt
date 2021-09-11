package com.killerinstinct.hobsapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.model.Request
import com.killerinstinct.hobsapp.model.Worker
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        CoroutineScope(Dispatchers.IO).launch{
            FirebaseFirestore.getInstance().collection("Worker")
                .document(requests[position].to)
                .get().addOnSuccessListener {
                    val worker = it.toObject(Worker::class.java)
                    holder.name.text = worker?.userName ?: "NULL"
                    if(worker == null || worker.profilePic.length < 5) {
                        holder.imageView.setImageDrawable(
                            AppCompatResources.getDrawable(
                                context,
                                R.drawable.ic_person
                            )
                        )
                        return@addOnSuccessListener
                    }
                    Glide.with(context)
                        .load(worker.profilePic)
                        .into(holder.imageView)
                }
        }
        holder.description.text = requests[position].description
    }

    override fun getItemCount() = requests.size

    inner class ShowRequestsViewHolder(view: View):
        RecyclerView.ViewHolder(view){
        val imageView = itemView.findViewById<CircleImageView>(R.id.request_image)
        val name = itemView.findViewById<TextView>(R.id.request_name)
        val description = itemView.findViewById<TextView>(R.id.request_description)
    }

}