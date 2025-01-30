package com.example.pr_voir_planner.repository

import com.example.pr_voir_planner.model.UserModel
import com.google.firebase.auth.FirebaseUser

interface UserRepository {

    //    {
//     "success" : true
//     "message": "login success"
//    "userId" "32323232323"
//    "statusCode": 200
//    }
    fun login(email:String,password:String,callback:(Boolean,String)->Unit)
    fun signup(email:String,password:String,callback:(Boolean,String,String)->Unit)
    fun addUserToDatabase(userId :String,userModel: UserModel,callback: (Boolean, String) -> Unit)
    fun forgetPassword(email: String,callback: (Boolean, String) -> Unit)
    fun getCurrentUser()  : FirebaseUser?
//     fun editProfile()
    // fun logout() etc
}