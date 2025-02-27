package com.example.pr_voir_planner.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
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
            // Open a new activity or fragment to add a task
            // For now, this will be a simple action
            taskList.add(Task("New Task", "Task description", "Medium", "2025-02-28 11:00 AM", "To-Do"))
            taskAdapter.notifyDataSetChanged()  // Refresh the RecyclerView
        }

        return view
    }
}
