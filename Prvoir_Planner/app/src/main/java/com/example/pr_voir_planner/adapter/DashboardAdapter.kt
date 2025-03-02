package com.example.pr_voir_planner.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pr_voir_planner.R
import com.example.pr_voir_planner.model.TaskModel

class DashboardAdapter(private val taskList: List<TaskModel>) : RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dashboard, parent, false)
        return DashboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val task = taskList[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    class DashboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        private val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
        private val textViewPriority: TextView = itemView.findViewById(R.id.textViewPriority)
        private val textViewPriorityValue: TextView = itemView.findViewById(R.id.textViewPriorityValue)

        fun bind(task: TaskModel) {
            textViewTitle.text = task.title
            textViewDate.text = task.date
            textViewPriority.text = "Priority: " // Static label
            textViewPriorityValue.text = task.priority

            // Set priority value color dynamically
            val context = itemView.context
            val priorityColor = when (task.priority?.lowercase()) {
                "high" -> ContextCompat.getColor(context, R.color.priority_high) // Red
                "medium" -> ContextCompat.getColor(context, R.color.priority_medium) // Yellow
                "low" -> ContextCompat.getColor(context, R.color.priority_low) // Green
                else -> ContextCompat.getColor(context, R.color.accent) // Fallback to accent (#FF5722)
            }
            textViewPriorityValue.setTextColor(priorityColor)
        }
    }
}