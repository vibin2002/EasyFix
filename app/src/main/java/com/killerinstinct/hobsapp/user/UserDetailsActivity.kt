package com.killerinstinct.hobsapp.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.killerinstinct.hobsapp.R
import com.killerinstinct.hobsapp.viewmodel.UserMainViewModel

class UserDetailsActivity : AppCompatActivity() {

    val viewModel: UserMainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)
    }
}