package com.killerinstinct.hobsapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.model.Review
import de.hdodenhof.circleimageview.CircleImageView

class ReviewAdapter(
    val context: Context,
    val list: List<Review>
): RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder(
            LayoutInflater.from(context).inflate(R.layout.review_card,parent,false)
        )
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.apply {
            name.text = list[position].userName
            descrption.text = list[position].review
            val timestamp = list[position].timestamp.toDate().toString().split(" ").toMutableList()
            timestamp.removeLast()
            timestamp.removeLast()
            date.text = timestamp.toList().toString().removeSuffix("]").removePrefix("[")
            if (list[position].userProPic.length < 5)
                image.setImageDrawable(
                    AppCompatResources.getDrawable(
                        context,
                        R.drawable.ic_person
                    )
                )
            else {
                Glide.with(context)
                    .load(list[position].userProPic)
                    .into(image)
            }
            rating.rating = list[position].rating.toFloat()
        }
    }

    override fun getItemCount() = list.size

    inner class ReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image = itemView.findViewById<CircleImageView>(R.id.review_propic)
        val name = itemView.findViewById<TextView>(R.id.review_name)
        val descrption = itemView.findViewById<TextView>(R.id.review_description)
        val date = itemView.findViewById<TextView>(R.id.rev_date)
        val rating = itemView.findViewById<AppCompatRatingBar>(R.id.review_ratingbar)
    }
}