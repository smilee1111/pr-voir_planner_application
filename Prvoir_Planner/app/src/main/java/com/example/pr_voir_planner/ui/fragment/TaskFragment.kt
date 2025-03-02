package com.example.pr_voir_planner.ui.fragment

import android.os.Bundle
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

        val taskRepository = TaskRepositoryImpl()
        taskViewModel = ViewModelProvider(
            this,
            TaskViewModelFactory(taskRepository)
        ).get(TaskViewModel::class.java)

        taskAdapter = TaskAdapter(
            taskList,
            onEditClick = { task -> showEditTaskDialog(task) },
            onDeleteClick = { task -> deleteTask(task) }
        )
        taskRecyclerView.adapter = taskAdapter
        taskRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        selectedDate = "$year-${month + 1}-$dayOfMonth"

        calendarView.date = calendar.timeInMillis
        loadTasksForDate(selectedDate)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = "$year-${month + 1}-$dayOfMonth"
            loadTasksForDate(selectedDate)
        }

        addTaskFab.setOnClickListener {
            val dialog = AddTaskDialog(selectedDate) { task ->
                taskViewModel.addTask(task) { success, message ->
                    if (success) {
                        loadTasksForDate(selectedDate)
                        Toast.makeText(requireContext(), "Task added successfully", Toast.LENGTH_SHORT).show()
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
                taskAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(requireContext(), "Failed to load tasks: $message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showEditTaskDialog(task: TaskModel) {
        val dialog = AddTaskDialog(selectedDate, task) { updatedTask ->
            taskViewModel.updateTask(updatedTask) { success, message -> // Changed to updateTask
                if (success) {
                    loadTasksForDate(selectedDate)
                    Toast.makeText(requireContext(), "Task updated successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        dialog.show(childFragmentManager, "EditTaskDialog")
    }

    private fun deleteTask(task: TaskModel) {
        task.taskId?.let { taskId ->
            taskViewModel.deleteTask(taskId) { success, message ->
                if (success) {
                    loadTasksForDate(selectedDate)
                    Toast.makeText(requireContext(), "Task deleted successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
        } ?: Toast.makeText(requireContext(), "Task ID not found", Toast.LENGTH_SHORT).show()
    }
}