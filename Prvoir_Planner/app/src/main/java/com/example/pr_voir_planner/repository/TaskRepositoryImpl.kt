package com.example.pr_voir_planner.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pr_voir_planner.model.TaskModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TaskRepositoryImpl : TaskRepository {

    private val database = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()

    override fun getTasksForUser(userId: String): LiveData<List<TaskModel>> {
        val tasksLiveData = MutableLiveData<List<TaskModel>>()
        database.child("tasks").child(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val tasks = mutableListOf<TaskModel>()
                    for (taskSnapshot in snapshot.children) {
                        val task = taskSnapshot.getValue(TaskModel::class.java)
                        if (task != null) {
                            tasks.add(task)
                        }
                    }
                    tasksLiveData.value = tasks
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        return tasksLiveData
    }

    override fun getCurrentUser() = auth.currentUser

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
            .orderByChild("date")
            .equalTo(date)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val tasks = mutableListOf<TaskModel>()
                    for (taskSnapshot in snapshot.children) {
                        val task = taskSnapshot.getValue(TaskModel::class.java)
                        if (task != null) {
                            tasks.add(task)
                        }
                    }
                    callback(true, tasks, "Tasks fetched successfully")
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, null, error.message)
                }
            })
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