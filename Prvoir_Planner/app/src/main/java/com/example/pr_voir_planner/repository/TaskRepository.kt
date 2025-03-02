package com.example.pr_voir_planner.repository

import com.example.pr_voir_planner.model.TaskModel


interface TaskRepository {
    fun addTask(task: TaskModel, callback: (Boolean, String) -> Unit)
    fun getTasksForUserAndDate(userId: String, date: String, callback: (Boolean, List<TaskModel>?, String) -> Unit)
    fun deleteTask(taskId: String, callback: (Boolean, String) -> Unit)
}

