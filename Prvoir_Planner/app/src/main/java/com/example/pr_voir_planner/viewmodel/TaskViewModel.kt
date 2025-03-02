package com.example.pr_voir_planner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.pr_voir_planner.model.TaskModel
import com.example.pr_voir_planner.repository.TaskRepository
import com.google.android.gms.tasks.Task

class TaskViewModel(private val repo: TaskRepository) : ViewModel() {

    fun addTask(task: TaskModel, callback: (Boolean, String) -> Unit) {
        repo.addTask(task, callback)
    }

    fun getTasksForUserAndDate(userId: String, date: String, callback: (Boolean, List<TaskModel>?, String) -> Unit) {
        repo.getTasksForUserAndDate(userId, date, callback)
    }
    fun updateTask(task: TaskModel, callback: (Boolean, String) -> Unit) { // Added
        repo.updateTask(task, callback)
    }
    fun getTasksForUser(userId: String): LiveData<List<TaskModel>> {
        return repo.getTasksForUser(userId)
    }

    fun getCurrentUser() = repo.getCurrentUser()
    fun deleteTask(taskId: String, callback: (Boolean, String) -> Unit) {
        repo.deleteTask(taskId, callback)
    }
}
