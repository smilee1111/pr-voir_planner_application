package com.example.pr_voir_planner.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pr_voir_planner.R
import com.example.pr_voir_planner.databinding.ActivityLoginBinding
import com.example.pr_voir_planner.repository.UserRepositoryImpl
import com.example.pr_voir_planner.viewmodel.UserViewModel

class LoginActivity : AppCompatActivity() {
    lateinit var binding:ActivityLoginBinding
    lateinit var userViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var repo =UserRepositoryImpl(this)
        userViewModel = UserViewModel(repo)
        binding.btnLogin.setOnClickListener {
            var email =binding.editEmail.text.toString()
            var password =binding.editPassword.text.toString()


            userViewModel.login(email,password){
                    success,message->
                if(success){
                    val intent = Intent (this@LoginActivity,NavigationActivity::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(this@LoginActivity,
                        message, Toast.LENGTH_LONG).show()
                }
            }

        }
        binding.btnSignupnavigate.setOnClickListener {
            val intent = Intent(this@LoginActivity,
                RegisterActivity::class.java)
            startActivity(intent)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    // In LoginActivity
    override fun onResume() {
        super.onResume()
        binding.editEmail.text.clear() // Clear the email field
        binding.editPassword.text.clear() // Clear the password field
    }

}