package com.example.pr_voir_planner.repository

import com.example.pr_voir_planner.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserRepositoryImpl : UserRepository{
    lateinit var auth: FirebaseAuth

    var database : FirebaseDatabase = FirebaseDatabase.getInstance()
    var ref : DatabaseReference =database.reference
        .child("users")

    override fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful){
                callback(true,"Login success")
            }else{
                callback(false,it.exception?.message.toString())
            }
        }
    }

    override fun signup(email: String, password: String, callback: (Boolean, String,String) -> Unit) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful){
                callback(true,"Registration success",auth.currentUser?.uid.toString())
            }else{
                callback(false,it.exception?.message.toString(),"")
            }
        }
    }

    override fun addUserToDatabase(
        userId: String,
        userModel: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(userId.toString()).setValue(userModel).addOnCompleteListener {
            if(it.isSuccessful){
                callback(true,"Registration success")
            }else{
                callback(false,it.exception?.message.toString())
            }
        }
    }

    override fun forgetPassword(
        email: String,
        callback: (Boolean, String) -> Unit
    ) {

    }

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
}