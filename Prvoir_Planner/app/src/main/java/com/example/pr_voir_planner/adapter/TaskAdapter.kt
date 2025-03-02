package com.example.pr_voir_planner.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pr_voir_planner.R
import com.example.pr_voir_planner.model.TaskModel

class TaskAdapter(private val tasks: List<TaskModel>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.taskTitle)
        val description: TextView = itemView.findViewById(R.id.taskDescription)
        val date: TextView = itemView.findViewById(R.id.taskDate) // Updated to use date
        val time: TextView = itemView.findViewById(R.id.taskTime) // Updated to use time
        val status: TextView = itemView.findViewById(R.id.taskStatus)
        val priority: TextView = itemView.findViewById(R.id.taskPriority)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.title.text = task.title
        holder.description.text = task.description
        holder.date.text = task.date // Set the date
        holder.time.text = task.time // Set the time
        holder.status.text = task.status
        holder.priority.text = task.priority
    }

    override fun getItemCount() = tasks.size
}