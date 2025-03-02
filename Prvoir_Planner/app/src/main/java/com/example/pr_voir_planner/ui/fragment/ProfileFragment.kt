package com.example.pr_voir_planner.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        // Initialize ViewModel
        val userRepository = UserRepositoryImpl(requireContext())
        val viewModelFactory = UserViewModelFactory(requireContext(),userRepository)
        userViewModel = ViewModelProvider(this, viewModelFactory).get(UserViewModel::class.java)

        binding.buttonLogout.setOnClickListener {
            userViewModel.logout { success, message ->
                // Handle the logout result here
                if (success) {
                    // Navigate to the login screen or show a success message
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    // Handle failure (e.g., show a toast or log the error message)
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
        }

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