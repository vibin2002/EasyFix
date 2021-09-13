package com.killerinstinct.hobsapp.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.model.Job
import com.killerinstinct.hobsapp.model.User
import com.killerinstinct.hobsapp.model.Worker
import com.killerinstinct.hobsapp.worker.fragments.WorkerJobsFragmentDirections
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkerJobsAdapter(
    val location: GeoPoint,
    val navController: NavController,
    val context: Context,
    val list: List<Job>,
    val userId: (Pair<String,String>) -> Unit
): RecyclerView.Adapter<WorkerJobsAdapter.WorkerJobViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkerJobViewHolder {
        return WorkerJobViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.worker_job_card,parent,false)
        )
    }

    override fun onBindViewHolder(holder: WorkerJobViewHolder, position: Int) {
        CoroutineScope(Dispatchers.IO).launch{
            FirebaseFirestore.getInstance().collection("User")
                .document(list[position].fromId)
                .get().addOnSuccessListener {
                    val user = it.toObject(User::class.java)
                    holder.workername.text = user?.userName ?: "NULL"
                    if(user == null)
                        return@addOnSuccessListener
                    if (user.profile.length < 5)
                    {
                        holder.image.setImageDrawable(
                            AppCompatResources.getDrawable(
                                context,
                                R.drawable.businessman
                            )
                        )
                    } else {
                        Glide.with(context)
                            .load(user.profile)
                            .into(holder.image)
                    }

                }
        }
        holder.time.text = list[position].reqTime+" , "+list[position].reqDate
        holder.description.text = list[position].description
        holder.markcompleted.setOnClickListener {
            userId(Pair(list[position].fromId,list[position].jobId))
        }
        holder.location.setOnClickListener {
            val action = WorkerJobsFragmentDirections.actionWorkerJobsFragmentToWorkerViewLocationFragment(
                list[position].location.latitude.toFloat(),
                list[position].location.longitude.toFloat(),
                list[position].fromName,
                location.latitude.toFloat(),
                location.longitude.toFloat(),
            )
            navController.navigate(action)
        }
        holder.call.setOnClickListener {
            val i = Intent(Intent.ACTION_DIAL)
            i.data = Uri.parse("tel:"+(list[position].contact))
            context.startActivity(i)
        }
        if (list[position].completed){
            holder.markcompleted.visibility = View.INVISIBLE
            holder.time.setTextColor(Color.parseColor("#04C652"))
            holder.markcompleted.visibility=View.VISIBLE
        }
    }

    override fun getItemCount() = list.size

    inner class WorkerJobViewHolder(view: View): RecyclerView.ViewHolder(view){
        val workername = itemView.findViewById<TextView>(R.id.jobwrkr_name)
        val time = itemView.findViewById<TextView>(R.id.jobwrkrtime)
        val markcompleted = itemView.findViewById<TextView>(R.id.tv_markcompleted)
        val description = itemView.findViewById<TextView>(R.id.jobwrkr_description)
        val image = itemView.findViewById<CircleImageView>(R.id.jobwrkr_image)
        val location = itemView.findViewById<TextView>(R.id.wrkrjobshowloc)
        val call = itemView.findViewById<TextView>(R.id.wrkrjobcalluser)
    }

}