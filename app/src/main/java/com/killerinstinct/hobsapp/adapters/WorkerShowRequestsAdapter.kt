package com.killerinstinct.hobsapp.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.GeoPoint
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.model.Request
import com.killerinstinct.hobsapp.worker.fragments.ShowRequestsFragmentDirections

class WorkerShowRequestsAdapter(
    private val workerLoc: GeoPoint,
    private val navController: NavController,
    private val requests: List<Request>,
    private val context: Context,
    private val decision: (Pair<Request,Boolean>) -> Unit
): RecyclerView.Adapter<WorkerShowRequestsAdapter.WorkerShowRequestsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkerShowRequestsViewHolder {
        return WorkerShowRequestsViewHolder(
            LayoutInflater.from(context).inflate(R.layout.worker_showreq_card,parent,false)
        )
    }

    override fun onBindViewHolder(holder: WorkerShowRequestsViewHolder, position: Int) {
        holder.name.text = requests[position].fromName
        holder.description.text = " Description : \n      ${requests[position].description}"
        holder.accept.setOnClickListener {
            decision(Pair(requests[position],true))
        }
        holder.decline.setOnClickListener {
            decision(Pair(requests[position],false))
        }
        holder.call.setOnClickListener {
            val i= Intent(Intent.ACTION_DIAL)
            i.data = Uri.parse("tel:"+(requests[position].contact))
            context.startActivity(i)
        }
        holder.showLocation.setOnClickListener {
            val action = ShowRequestsFragmentDirections.actionShowRequestsFragmentToWorkerViewLocationFragment(
                requests[position].location.latitude.toFloat(),
                requests[position].location.longitude.toFloat(),
                requests[position].fromName,
                workerLoc.latitude.toFloat(),
                workerLoc.longitude.toFloat(),
            )
            navController.navigate(action)
        }
    }

    override fun getItemCount() = requests.size

    inner class WorkerShowRequestsViewHolder(view: View): RecyclerView.ViewHolder(view){
        val name = itemView.findViewById<TextView>(R.id.req_name)
        val description = itemView.findViewById<TextView>(R.id.req_description)
        val showLocation = itemView.findViewById<TextView>(R.id.showloc)
        val accept = itemView.findViewById<TextView>(R.id.btn_accept)
        val decline = itemView.findViewById<TextView>(R.id.btn_decline)
        val call = itemView.findViewById<TextView>(R.id.calluser)
    }

}