package com.example.pr_voir_planner.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pr_voir_planner.databinding.FragmentProfileBinding
import com.example.pr_voir_planner.repository.UserRepositoryImpl
import com.example.pr_voir_planner.ui.activity.LoginActivity
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
        binding.buttonLogout.setOnClickListener {
            userViewModel.logout()

            // Create an intent to navigate to the LoginActivity
            val intent = Intent(requireContext(), LoginActivity::class.java)

            // Clear the back stack so the user cannot go back to the profile screen
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            // Start the LoginActivity
            startActivity(intent)

            // Close the current activity
            requireActivity().finish()
        }

        // Initialize ViewModel
        val userRepository = UserRepositoryImpl()
        val viewModelFactory = UserViewModelFactory(userRepository)
        userViewModel = ViewModelProvider(this, viewModelFactory).get(UserViewModel::class.java)

        // Fetch current user ID
        val userId = userViewModel.getCurrentUser()?.uid
        if (userId != null) {
            Log.d("ProfileFragment", "Fetching tasks for userId: $userId")
            userViewModel.getUserData(userId) { userModel ->
                if (userModel != null) {


                    binding.textViewWelcome.text = "Welcome, ${userModel.firstName}!"
                    binding.textViewFirstName.text = userModel.firstName
                    binding.textViewLastName.text = userModel.lastName
                    binding.textViewEmail.text = "Email: ${userModel.email}"
                    binding.textViewContact.text = "Contact: ${userModel.contact}"
                    binding.textViewAddress.text = "Address: ${userModel.address}"
                } else {
                    binding.textViewFirstName.text = "User not found"
                    binding.textViewLastName.text = ""
                    binding.textViewEmail.text = "Email: N/A"
                    binding.textViewContact.text = "Contact: N/A"
                    binding.textViewAddress.text = "Address: N/A"
                }
            }
        } else {
            binding.textViewFirstName.text = "Not logged in"
            binding.textViewLastName.text = ""
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}