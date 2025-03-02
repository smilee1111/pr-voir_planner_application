package com.example.pr_voir_planner.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pr_voir_planner.model.TaskModel
import com.example.pr_voir_planner.model.UserModel
import com.example.pr_voir_planner.repository.UserRepository
import com.google.firebase.auth.FirebaseUser

class UserViewModel(private val repo: UserRepository) : ViewModel() { // Made repo private
    fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        repo.login(email, password, callback)
    }

    fun signup(email: String, password: String, callback: (Boolean, String, String) -> Unit) {
        repo.signup(email, password, callback)
    }

    fun addUserToDatabase(userId: String, userModel: UserModel, callback: (Boolean, String) -> Unit) {
        repo.addUserToDatabase(userId, userModel, callback)
    }
    fun getUserData(userId: String, callback: (UserModel?) -> Unit) {
        repo.getUserData(userId, callback)
    }
    fun forgetPassword(email: String, callback: (Boolean, String) -> Unit) {
        repo.forgetPassword(email, callback)
    }

    fun getCurrentUser(): FirebaseUser? {
        return repo.getCurrentUser()
    }

    fun logout(callback: (Boolean, String) -> Unit) {
        // Pass the callback to the repository's logout method
        repo.logout(callback)
    }
}
