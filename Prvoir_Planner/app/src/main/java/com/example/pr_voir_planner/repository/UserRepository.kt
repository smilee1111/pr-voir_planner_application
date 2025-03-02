package com.example.pr_voir_planner.repository

import com.example.pr_voir_planner.model.TaskModel
import com.example.pr_voir_planner.model.UserModel
import com.google.firebase.auth.FirebaseUser

interface UserRepository {
    fun login(email: String, password: String, callback: (Boolean, String) -> Unit)
    fun signup(email: String, password: String, callback: (Boolean, String, String) -> Unit)
    fun addUserToDatabase(userId: String, userModel: UserModel, callback: (Boolean, String) -> Unit)
    fun forgetPassword(email: String, callback: (Boolean, String) -> Unit)
    fun getCurrentUser(): FirebaseUser?
    fun getUserData(userId: String, callback: (UserModel?) -> Unit) // NEW FUNCTION
    fun getTasksForUser(userId: String, callback: (List<TaskModel>) -> Unit) // NEW FUNCTION
    fun logout(callback: (Boolean, String) -> Unit) // NEW FUNCTION
}
