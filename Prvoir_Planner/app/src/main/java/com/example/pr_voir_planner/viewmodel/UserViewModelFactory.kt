package com.example.pr_voir_planner.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pr_voir_planner.repository.UserRepository

class UserViewModelFactory(private val context: Context, private val repo: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}