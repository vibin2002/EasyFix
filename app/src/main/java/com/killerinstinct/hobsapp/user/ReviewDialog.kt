package com.killerinstinct.hobsapp.user

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.fragment.app.activityViewModels
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Timestamp
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.model.Review
import com.killerinstinct.hobsapp.viewmodel.UserMainViewModel

class ReviewDialog(
    val userId: String,
    val userName: String,
    val userPropic: String,
    val workerId: String
): AppCompatDialogFragment() {

    private val viewModel: UserMainViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val view = requireActivity().layoutInflater.inflate(R.layout.review_dialog,null)
        val ratingBar = view.findViewById<AppCompatRatingBar>(R.id.appCompatRatingBar)
        val reviewEt = view.findViewById<TextInputEditText>(R.id.et_review)

        builder.setView(view)
            .setTitle("Rating")
            .setPositiveButton("Post") { dialog, i ->
                val review = Review(
                    userId,
                    reviewEt.text.toString(),
                    ratingBar.rating.toString(),
                    userName,
                    userPropic,
                    Timestamp.now()
                )
                postReview(review,workerId)
            }
            .setNegativeButton("Cancel") { dialog, i ->
                dismiss()
            }
        return builder.create()
    }

    private fun postReview(review: Review,workerId: String) {
        viewModel.postReview(review,workerId)
    }

}