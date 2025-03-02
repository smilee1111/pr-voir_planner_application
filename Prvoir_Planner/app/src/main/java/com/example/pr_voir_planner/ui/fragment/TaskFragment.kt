package com.example.pr_voir_planner.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pr_voir_planner.R
import com.example.pr_voir_planner.adapter.TaskAdapter
import com.example.pr_voir_planner.model.TaskModel
import com.example.pr_voir_planner.ui.dialog.AddTaskDialog
import com.example.pr_voir_planner.viewmodel.TaskViewModel
import com.example.pr_voir_planner.viewmodel.TaskViewModelFactory
import com.example.pr_voir_planner.repository.TaskRepositoryImpl
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class TaskFragment : Fragment() {

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var calendarView: CalendarView
    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var addTaskFab: FloatingActionButton
    private lateinit var taskAdapter: TaskAdapter
    private val taskList = mutableListOf<TaskModel>()

    private var selectedDate: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_task, container, false)

        calendarView = view.findViewById(R.id.taskCalendarView)
        taskRecyclerView = view.findViewById(R.id.taskRecyclerView)
        addTaskFab = view.findViewById(R.id.addTaskFab)

        // Initialize TaskRepository
        val taskRepository = TaskRepositoryImpl()
        // Use TaskViewModelFactory to create the ViewModel instance
        taskViewModel = ViewModelProvider(
            this,
            TaskViewModelFactory(taskRepository)
        ).get(TaskViewModel::class.java)

        taskAdapter = TaskAdapter(taskList)
        taskRecyclerView.adapter = taskAdapter
        taskRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize selectedDate with the current date
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        selectedDate = "$year-${month + 1}-$dayOfMonth" // Format: YYYY-MM-DD

        // Set the calendar to the current date
        calendarView.date = calendar.timeInMillis

        // Load tasks for the current date
        loadTasksForDate(selectedDate)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = "$year-${month + 1}-$dayOfMonth" // Format: YYYY-MM-DD
            loadTasksForDate(selectedDate)
        }

        addTaskFab.setOnClickListener {
            val dialog = AddTaskDialog(selectedDate) { task ->
                taskViewModel.addTask(task) { success, message ->
                    if (success) {
                        // Reload tasks for the selected date after adding a new task
                        loadTasksForDate(selectedDate)
                    } else {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            dialog.show(childFragmentManager, "AddTaskDialog")
        }

        return view
    }

    private fun loadTasksForDate(date: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Toast.makeText(requireContext(), "User not authenticated", Toast.LENGTH_SHORT).show()
            return
        }

        taskViewModel.getTasksForUserAndDate(userId, date) { success, tasks, message ->
            if (success) {
                taskList.clear()
                taskList.addAll(tasks ?: emptyList())
                taskAdapter.notifyDataSetChanged() // Notify the adapter of data changes
            } else {
                Toast.makeText(requireContext(), "Failed to load tasks: $message", Toast.LENGTH_SHORT).show()
            }
        }
    }
}