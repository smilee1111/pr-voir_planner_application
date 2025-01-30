package com.example.pr_voir_planner.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pr_voir_planner.R
import com.example.pr_voir_planner.databinding.ActivityRegisterBinding
import com.example.pr_voir_planner.model.UserModel
import com.example.pr_voir_planner.repository.UserRepositoryImpl
import com.example.pr_voir_planner.viewmodel.UserViewModel

class RegisterActivity : AppCompatActivity() {
    lateinit var binding:ActivityRegisterBinding


    lateinit var userViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var repo =UserRepositoryImpl()
        userViewModel = UserViewModel(repo)
        binding.signUp.setOnClickListener {
            var email =binding.registerEmail.text.toString()
            var password =binding.registerPassword.text.toString()
            var firstName =binding.registerFname.text.toString()
            var lastName =binding.registerLName.text.toString()
            var address =binding.registerAddress.text.toString()
            var contact =binding.registerContact.text.toString()

            userViewModel.signup(email,password){
                    success,message,userId->
                if(success){
                    var userModel =UserModel(
                        userId.toString()
                        ,
                        firstName, lastName, address, contact,email
                    )
                    userViewModel.addUserToDatabase(userId.toString(),userModel){
                            success,message->
                        if(success){

                        }else{

                        }
                    }
                    Toast.makeText(
                        this@RegisterActivity,
                        message, Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(
                        this@RegisterActivity,
                        message, Toast.LENGTH_LONG
                    ).show()
                }
            }
//            auth.createUserWithEmailAndPassword(email,password)
//                .addOnCompleteListener {
//                    if(it.isSuccessful){
//                        var userId = auth.currentUser?.uid
//
//                        var userModel =UserModel(
//                            userId.toString()
//                            ,
//                            firstName, lastName, address, contact,email
//                        )
//
//                        ref.child(userId.toString()).setValue(userModel).addOnCompleteListener {
//                            if(it.isSuccessful){
//                                Toast.makeText(this@RegisterActivity,
//                                    "Register Success",Toast.LENGTH_LONG)
//                                    .show()
//                            }else{
//                                Toast.makeText(this@RegisterActivity, it.exception?.message.toString(),
//                                    Toast.LENGTH_LONG).show()
//                            }
//                            }t
//                        }else{
//                        Toast.makeText(this@RegisterActivity, it.exception?.message.toString(),
//                            Toast.LENGTH_LONG).show()
//                    }
//
//                }
        }
        binding.btnAlogin.setOnClickListener {
            val intent = Intent(this@RegisterActivity,
                LoginActivity::class.java)
            startActivity(intent)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}