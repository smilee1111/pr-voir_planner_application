package com.example.pr_voir_planner.repository

import com.example.pr_voir_planner.model.TaskModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class TaskRepositoryImpl : TaskRepository {

    private val database = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()

    override fun addTask(task: TaskModel, callback: (Boolean, String) -> Unit) {
        val userId = auth.currentUser?.uid ?: return callback(false, "User not authenticated")
        val taskId = database.child("tasks").child(userId).push().key ?: return callback(false, "Failed to generate task ID")

        task.taskId = taskId
        task.userId = userId

        database.child("tasks").child(userId).child(taskId).setValue(task)
            .addOnSuccessListener {
                callback(true, "Task added successfully")
            }
            .addOnFailureListener { e ->
                callback(false, e.message ?: "Failed to add task")
            }
    }

    override fun getTasksForUserAndDate(userId: String, date: String, callback: (Boolean, List<TaskModel>?, String) -> Unit) {
        database.child("tasks").child(userId)
            .orderByChild("date").equalTo(date) // Fetch tasks for the selected date
            .get()
            .addOnSuccessListener { dataSnapshot ->
                val tasks = mutableListOf<TaskModel>()
                for (snapshot in dataSnapshot.children) {
                    val task = snapshot.getValue(TaskModel::class.java)
                    if (task != null) {
                        tasks.add(task)
                    }
                }
                callback(true, tasks, "Tasks fetched successfully")
            }
            .addOnFailureListener { e ->
                callback(false, null, e.message ?: "Failed to fetch tasks")
            }
    }

    override fun deleteTask(taskId: String, callback: (Boolean, String) -> Unit) {
        val userId = auth.currentUser?.uid ?: return callback(false, "User not authenticated")
        database.child("tasks").child(userId).child(taskId).removeValue()
            .addOnSuccessListener {
                callback(true, "Task deleted successfully")
            }
            .addOnFailureListener { e ->
                callback(false, e.message ?: "Failed to delete task")
            }
    }
}