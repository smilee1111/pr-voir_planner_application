package com.example.pr_voir_planner.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pr_voir_planner.R
import com.example.pr_voir_planner.ui.adapter.TaskAdapter
import com.example.pr_voir_planner.model.Task

class TaskFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var addTaskButton: Button
    private lateinit var taskAdapter: TaskAdapter
    private var taskList = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize task list with dummy data or load from database
        taskList.add(Task("Sample Task", "Description of task", "High", "2025-02-27 10:00 AM", "To-Do"))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_activity, container, false)

        // Initialize RecyclerView and Button
        recyclerView = view.findViewById(R.id.taskRecyclerView)
        addTaskButton = view.findViewById(R.id.addTaskButton)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        taskAdapter = TaskAdapter(taskList)
        recyclerView.adapter = taskAdapter

        // Set up Add Task Button click listener
        addTaskButton.setOnClickListener {
            showAddTaskDialog()
        }

        return view
    }

    // Function to show the Add Task Dialog
    private fun showAddTaskDialog() {
        // Inflate the dialog view
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_task, null)

        // Get references to the input fields
        val taskTitleEditText = dialogView.findViewById<EditText>(R.id.editTaskTitle)
        val taskDescriptionEditText = dialogView.findViewById<EditText>(R.id.editTaskDescription)
        val taskPriorityEditText = dialogView.findViewById<EditText>(R.id.editTaskPriority)
        val taskDueTimeEditText = dialogView.findViewById<EditText>(R.id.editTaskDueTime)
        val taskStatusEditText = dialogView.findViewById<EditText>(R.id.editTaskStatus)

        // Build the dialog
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Add New Task")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                // Get the data from the input fields
                val title = taskTitleEditText.text.toString()
                val description = taskDescriptionEditText.text.toString()
                val priority = taskPriorityEditText.text.toString()
                val dueTime = taskDueTimeEditText.text.toString()
                val status = taskStatusEditText.text.toString()

                // Create a new Task and add it to the list
                val newTask = Task(title, description, priority, dueTime, status)
                taskList.add(newTask)

                // Notify the adapter that a new item was inserted
                taskAdapter.notifyItemInserted(taskList.size - 1)
            }
            .setNegativeButton("Cancel", null) // Dismiss the dialog if canceled
            .create()

        // Show the dialog
        dialog.show()
    }
}
