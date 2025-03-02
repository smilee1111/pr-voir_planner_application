package com.example.pr_voir_planner.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pr_voir_planner.R
import com.example.pr_voir_planner.databinding.FragmentProfileBinding
import com.example.pr_voir_planner.repository.UserRepositoryImpl
import com.example.pr_voir_planner.viewmodel.UserViewModel
import com.example.pr_voir_planner.viewmodel.UserViewModelFactory

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        val userRepository = UserRepositoryImpl() // Create repository instance
        val viewModelFactory = UserViewModelFactory(userRepository) // Create factory
        userViewModel = ViewModelProvider(this, viewModelFactory).get(UserViewModel::class.java)

        // Fetch current user ID
        val userId = userViewModel.getCurrentUser()?.uid
        if (userId != null) {
            // Fetch user data from Firebase
            userViewModel.getUserData(userId) { userModel ->
                if (userModel != null) {
                    // Update UI with user's name
                    binding.textViewFirstName.text = userModel.firstName
                    binding.textViewLastName.text = userModel.lastName

                    // Fetch tasks for the user
                    userViewModel.getTasksForUser(userId) { tasks ->
                        // Calculate progress
                        val totalTasks = tasks.size
                        val completedTasks = tasks.count { it.status == "Done" }

                        // Update progress bar and percentage
                        val progress = if (totalTasks > 0) {
                            (completedTasks.toFloat() / totalTasks.toFloat()) * 100
                        } else {
                            0f
                        }
                        binding.progressBar.progress = progress.toInt()
                        binding.textViewProgressPercentage.text = "${progress.toInt()}%"
                    }
                } else {
                    // Handle error (e.g., user data not found)
                    binding.textViewFirstName.text = "User not found"
                    binding.textViewLastName.text = ""
                    binding.progressBar.progress = 0
                    binding.textViewProgressPercentage.text = "0%"
                }
            }
        } else {
            // Handle error (e.g., user not logged in)
            binding.textViewFirstName.text = "Not logged in"
            binding.textViewLastName.text = ""
            binding.progressBar.progress = 0
            binding.textViewProgressPercentage.text = "0%"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}