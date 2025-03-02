package com.example.pr_voir_planner.repository

import androidx.lifecycle.LiveData
import com.example.pr_voir_planner.model.TaskModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser


interface TaskRepository {
    fun addTask(task: TaskModel, callback: (Boolean, String) -> Unit)
    fun getTasksForUserAndDate(userId: String, date: String, callback: (Boolean, List<TaskModel>?, String) -> Unit)
    fun deleteTask(taskId: String, callback: (Boolean, String) -> Unit)
    fun getTasksForUser(userId: String): LiveData<List<TaskModel>>
    fun getCurrentUser(): FirebaseUser?
}

