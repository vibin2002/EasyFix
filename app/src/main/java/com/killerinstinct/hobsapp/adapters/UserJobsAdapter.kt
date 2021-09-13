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
import com.killerinstinct.hobsapp.model.Job
import com.killerinstinct.hobsapp.model.Worker
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserJobsAdapter(
    val context: Context,
    val list: List<Job>,
    val workerId: (String) -> Unit
): RecyclerView.Adapter<UserJobsAdapter.JobViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        return JobViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_job_card,parent,false)
        )
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        CoroutineScope(Dispatchers.IO).launch{
            FirebaseFirestore.getInstance().collection("Worker")
                .document(list[position].toId)
                .get().addOnSuccessListener {
                    val worker = it.toObject(Worker::class.java)
                    holder.workername.text = worker?.userName ?: "NULL"
                    if(worker == null)
                        return@addOnSuccessListener
                    if (worker.profilePic.length < 5)
                    {
                        holder.image.setImageDrawable(
                            AppCompatResources.getDrawable(
                                context,
                                R.drawable.businessman
                            )
                        )
                    } else {
                        Glide.with(context)
                            .load(worker.profilePic)
                            .into(holder.image)
                    }
                }
        }
        holder.image.setImageDrawable(
            AppCompatResources.getDrawable(
                context,
                R.drawable.businessman
            )
        )
        holder.time.text = list[position].reqTime
        holder.date.text = list[position].reqDate
        holder.description.text = list[position].description
        holder.review.setOnClickListener {
            workerId(list[position].toId)
        }
    }

    override fun getItemCount() = list.size

    inner class JobViewHolder(view: View): RecyclerView.ViewHolder(view){
        val workername = itemView.findViewById<TextView>(R.id.job_name)
        val time = itemView.findViewById<TextView>(R.id.jobtime)
        val date = itemView.findViewById<TextView>(R.id.jobdate)
        val review = itemView.findViewById<TextView>(R.id.tv_review)
        val description = itemView.findViewById<TextView>(R.id.job_description)
        val image = itemView.findViewById<CircleImageView>(R.id.job_image)
    }

}