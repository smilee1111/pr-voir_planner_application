package com.example.pr_voir_planner.viewmodel

import com.example.pr_voir_planner.model.UserModel
import com.example.pr_voir_planner.repository.UserRepository
import com.google.firebase.auth.FirebaseUser

class UserViewModel(private val repo: UserRepository) { // Made repo private
    fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        repo.login(email, password, callback)
    }

    fun signup(email: String, password: String, callback: (Boolean, String, String) -> Unit) {
        repo.signup(email, password, callback)
    }

    fun addUserToDatabase(userId: String, userModel: UserModel, callback: (Boolean, String) -> Unit) {
        repo.addUserToDatabase(userId, userModel, callback)
    }

    fun forgetPassword(email: String, callback: (Boolean, String) -> Unit) {
        repo.forgetPassword(email, callback)
    }

    fun getCurrentUser(): FirebaseUser? {
        return repo.getCurrentUser()
    }
}
