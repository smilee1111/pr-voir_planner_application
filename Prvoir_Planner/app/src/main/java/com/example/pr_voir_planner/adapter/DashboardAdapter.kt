package com.example.pr_voir_planner.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

        fun bind(task: TaskModel) {
            textViewTitle.text = task.title
            textViewDate.text = task.date
            textViewPriority.text = "Priority: ${task.priority}"
        }
    }
}