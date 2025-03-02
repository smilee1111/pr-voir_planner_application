package com.example.pr_voir_planner.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pr_voir_planner.R
import com.example.pr_voir_planner.model.TaskModel

class TaskAdapter(private val tasks: List<TaskModel>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.taskTitle)
        val description: TextView = itemView.findViewById(R.id.taskDescription)
        val date: TextView = itemView.findViewById(R.id.taskDate)
        val time: TextView = itemView.findViewById(R.id.taskTime)
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
        holder.date.text = task.date
        holder.time.text = task.time
        holder.status.text = task.status
        holder.priority.text = task.priority

        // Set priority color dynamically
        val context = holder.itemView.context
        val priorityColor = when (task.priority?.lowercase()) {
            "high" -> ContextCompat.getColor(context, R.color.priority_high) // Red
            "medium" -> ContextCompat.getColor(context, R.color.priority_medium) // Yellow
            "low" -> ContextCompat.getColor(context, R.color.priority_low) // Green
            else -> ContextCompat.getColor(context, R.color.accent) // Fallback to accent (#FF5722)
        }
        holder.priority.setTextColor(priorityColor)
    }

    override fun getItemCount() = tasks.size
}