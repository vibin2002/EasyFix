package com.killerinstinct.hobsapp.adapters

import android.content.Context
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
import com.killerinstinct.hobsapp.worker.fragments.WorkerSearchFragmentDirections
import de.hdodenhof.circleimageview.CircleImageView

class SearchAdapter(
    private val navController: NavController,
    private val context: Context,
    private val workerList: MutableList<Worker>,
    private val workerListAll: List<Worker> = workerList.toList()
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>(), Filterable {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.search_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.apply {
            itemView.setOnClickListener {
                val action =
                    WorkerSearchFragmentDirections.actionWorkerNavigationSearchToShowProfileFragment(
                        workerList[position].uid // UID to be passed
                    )
                navController.navigate(action)
            }
            if (workerList[position].profilePic == "")
                propic.setImageDrawable(
                    AppCompatResources.getDrawable(
                        context,
                        R.drawable.ic_person
                    )
                )
            else {
                Glide.with(context)
                    .load(workerList[position].profilePic)
                    .into(propic)
            }
            name.text = workerList[position].userName
            designation.text = workerList[position].category.toString()
        }
    }

    override fun getItemCount() = workerList.size

    inner class SearchViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {
        val propic = itemView.findViewById<CircleImageView>(R.id.search_propic)
        val name = itemView.findViewById<TextView>(R.id.search_name)
        val designation = itemView.findViewById<TextView>(R.id.search_designation)
    }

    override fun getFilter(): Filter {
        return searchFilter
    }

    private val searchFilter = object : Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val list = mutableListOf<Worker>()
            if (charSequence.isEmpty()) {
                list.addAll(workerListAll)
            } else {
                for (worker in workerListAll) {
                    if (worker.userName.lowercase().contains(charSequence.toString().lowercase())) {
                        list.add(worker)
                    }
                }
            }
            val filterResult = FilterResults().apply {
                values = list
            }
            return filterResult
        }

        override fun publishResults(p0: CharSequence?, filterRes: FilterResults?) {
            workerList.apply {
                clear()
                if (filterRes != null) {
                    addAll(filterRes.values as Collection<Worker>)
                }
                notifyDataSetChanged()
            }
        }

    }
}