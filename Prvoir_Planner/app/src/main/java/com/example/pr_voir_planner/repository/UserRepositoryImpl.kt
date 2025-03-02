package com.example.pr_voir_planner.repository

import android.util.Log
import com.example.pr_voir_planner.model.TaskModel
import com.example.pr_voir_planner.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserRepositoryImpl : UserRepository {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var ref: DatabaseReference = database.reference.child("users")
    private var tasksRef: DatabaseReference = database.reference.child("tasks")

    // Existing functions...

    override fun getTasksForUser(userId: String, callback: (List<TaskModel>) -> Unit) {
        tasksRef.orderByChild("userId").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("UserRepositoryImpl", "Tasks snapshot: $snapshot")
                    val tasks = mutableListOf<TaskModel>()
                    for (taskSnapshot in snapshot.children) {
                        val task = taskSnapshot.getValue(TaskModel::class.java)
                        if (task != null) {
                            tasks.add(task)
                        }
                    }
                    callback(tasks)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("UserRepositoryImpl", "Error fetching tasks: ${error.message}")
                    callback(emptyList())
                }
            })
    }
    override fun getUserData(userId: String, callback: (UserModel?) -> Unit) {
        ref.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("UserRepositoryImpl", "User data snapshot: $snapshot")
                val userModel = snapshot.getValue(UserModel::class.java)
                callback(userModel)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("UserRepositoryImpl", "Error fetching user data: ${error.message}")
                callback(null)
            }
        })
    }
    override fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Login success")
            } else {
                callback(false, it.exception?.message.toString())
            }
        }
    }

    override fun signup(email: String, password: String, callback: (Boolean, String, String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Registration success", auth.currentUser?.uid.toString())
            } else {
                callback(false, it.exception?.message.toString(), "")
            }
        }
    }

    override fun addUserToDatabase(userId: String, userModel: UserModel, callback: (Boolean, String) -> Unit) {
        ref.child(userId).setValue(userModel).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Registration success")
            } else {
                callback(false, it.exception?.message.toString())
            }
        }
    }

    override fun forgetPassword(email: String, callback: (Boolean, String) -> Unit) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Reset email sent successfully")
            } else {
                callback(false, it.exception?.message.toString())
            }
        }
    }

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override fun logout(callback: (Boolean, String) -> Unit) {
        auth.signOut()
        if (auth.currentUser == null) {
            callback(true, "Logout successful")
        } else {
            callback(false, "Logout failed")
        }
    }
}