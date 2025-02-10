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
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var userRepository: UserRepositoryImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userRepository = UserRepositoryImpl()
        userViewModel = UserViewModel(userRepository)

        binding.signUp.setOnClickListener {
            val email = binding.registerEmail.text.toString().trim()
            val password = binding.registerPassword.text.toString().trim()
            val firstName = binding.registerFname.text.toString().trim()
            val lastName = binding.registerLName.text.toString().trim()
            val address = binding.registerAddress.text.toString().trim()
            val contact = binding.registerContact.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || address.isEmpty() || contact.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            userViewModel.signup(email, password) { success, message, userId ->
                if (success) {
                    val userModel = UserModel(userId, firstName, lastName, address, contact, email)
                    userViewModel.addUserToDatabase(userId, userModel) { dbSuccess, dbMessage ->
                        if (dbSuccess) {
                            Toast.makeText(this, "Registration successful", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, dbMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.btnAlogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
