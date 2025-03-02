package com.example.pr_voir_planner.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pr_voir_planner.databinding.FragmentHomeBinding
import com.example.pr_voir_planner.model.TaskModel
import com.example.pr_voir_planner.model.UserModel
import com.example.pr_voir_planner.repository.TaskRepositoryImpl
import com.example.pr_voir_planner.repository.UserRepositoryImpl
import com.example.pr_voir_planner.ui.adapter.DashboardAdapter
import com.example.pr_voir_planner.viewmodel.TaskViewModel
import com.example.pr_voir_planner.viewmodel.TaskViewModelFactory
import com.example.pr_voir_planner.viewmodel.UserViewModel
import com.example.pr_voir_planner.viewmodel.UserViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var dashboardAdapter: DashboardAdapter
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Task ViewModel
        val taskRepository = TaskRepositoryImpl()
        val taskViewModelFactory = TaskViewModelFactory(taskRepository)
        taskViewModel = ViewModelProvider(this, taskViewModelFactory).get(TaskViewModel::class.java)

        // Initialize User ViewModel
        val userRepository = UserRepositoryImpl(requireContext())
        val userViewModelFactory = UserViewModelFactory(requireContext(),userRepository)
        userViewModel = ViewModelProvider(this, userViewModelFactory).get(UserViewModel::class.java)

        // Set up RecyclerView
        dashboardAdapter = DashboardAdapter(emptyList())
        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewTasks.adapter = dashboardAdapter

        // Fetch and display the current user's name
        fetchCurrentUserName()

        // Fetch tasks for the current user
        fetchTasks()
    }

    private fun fetchCurrentUserName() {
        val userId = taskViewModel.getCurrentUser()?.uid
        if (userId != null) {
            userViewModel.getUserData(userId) { userModel ->
                if (userModel != null) {
                    // Update the greeting message with the user's first name
                    binding.textViewGreeting.text = "Good Morning, ${userModel.firstName}!"
                } else {
                    binding.textViewGreeting.text = "Good Morning, User!"
                }
            }
        } else {
            binding.textViewGreeting.text = "Good Morning, User!"
        }
    }

    private fun fetchTasks() {
        val userId = taskViewModel.getCurrentUser()?.uid
        if (userId != null) {
            taskViewModel.getTasksForUser(userId).observe(viewLifecycleOwner) { tasks ->
                if (tasks != null) {
                    // Update RecyclerView with real data
                    dashboardAdapter = DashboardAdapter(tasks)
                    binding.recyclerViewTasks.adapter = dashboardAdapter

                    // Update progress
                    updateProgress(tasks)
                }
            }
        }
    }

    private fun updateProgress(tasks: List<TaskModel>) {
        val totalTasks = tasks.size
        val completedTasks = tasks.count { it.status == "Done" }
        val progress = if (totalTasks > 0) (completedTasks.toFloat() / totalTasks) * 100 else 0

        binding.textViewProgress.text = "Progress: ${progress.toInt()}%"
        binding.progressBar.progress = progress.toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}