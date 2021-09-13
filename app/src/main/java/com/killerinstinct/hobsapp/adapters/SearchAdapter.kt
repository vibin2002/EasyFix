package com.killerinstinct.hobsapp.adapters

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.model.Worker
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class SearchAdapter(
    val view: View,
    private val context: Context,
    private val workerList: MutableList<Worker>,
    val workerid: (String) -> Unit,
    ) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>(),Filterable {

    var workerListFiltered=ArrayList<Worker>()
    init {
        workerListFiltered=workerList as ArrayList<Worker>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.search_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        view.visibility = View.GONE
        holder.apply {
            itemView.setOnClickListener {
                Log.d("Helloworld", "onBindViewHolder: ${workerListFiltered[position].uid}")
                workerid(workerListFiltered[position].uid)
            }
            if (workerListFiltered[position].profilePic == "")
                propic.setImageDrawable(
                    AppCompatResources.getDrawable(
                        context,
                        R.drawable.ic_person
                    )
                )
            else {
                Glide.with(context)
                    .load(workerListFiltered[position].profilePic)
                    .into(propic)
            }
            name.text = workerListFiltered[position].userName
            rating.text = workerListFiltered[position].rating
            var address: Address? = null
            val geocoder = Geocoder(context)
            val addresses = geocoder.getFromLocation(
                workerListFiltered[position].location.latitude,
                workerListFiltered[position].location.longitude,
                1
            )
            if(addresses.isNotEmpty()){
                address = addresses[0]
                location.text = "${address.subLocality}, ${address.locality}"
            }
        }
    }

    override fun getItemCount() = workerListFiltered.size

    inner class SearchViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {
        val propic = itemView.findViewById<CircleImageView>(R.id.search_propic)
        val name = itemView.findViewById<TextView>(R.id.search_name)
        val location = itemView.findViewById<TextView>(R.id.search_location)
        val rating = itemView.findViewById<TextView>(R.id.search_rating)
    }

    override fun getFilter(): Filter {
        return searchFilter
    }

    private val searchFilter = object : Filter() {
        override fun performFiltering(charSequence: CharSequence?): FilterResults {
            val str=charSequence.toString()
            if (str.isEmpty()) {
                workerListFiltered=workerList as ArrayList<Worker>
            } else {
                val list = ArrayList<Worker>()
                for (worker in workerList) {
                    if (worker.userName.toLowerCase().contains(charSequence.toString().toLowerCase())
                    || worker.category.contains(charSequence.toString())) {
                        list.add(worker)
                        Log.e("FilterResultinFirebase",worker.userName)
                    }
                }
                workerListFiltered=list
            }
            val filterResult = FilterResults()
            filterResult.values=workerListFiltered
            return filterResult
        }

        override fun publishResults(p0: CharSequence?, filterRes: FilterResults?) {
            workerListFiltered=filterRes?.values as ArrayList<Worker>
            if (workerListFiltered.isEmpty()){
                view.visibility = View.VISIBLE
            }
            notifyDataSetChanged()
        }

    }
}