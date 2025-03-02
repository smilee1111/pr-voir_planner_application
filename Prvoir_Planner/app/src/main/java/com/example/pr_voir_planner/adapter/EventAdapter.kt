package com.example.pr_voir_planner.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pr_voir_planner.R
import com.example.pr_voir_planner.model.EventModel

class EventAdapter(
    private val events: List<EventModel>,
    private val onEditClick: (EventModel) -> Unit,
    private val onDeleteClick: (EventModel) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.eventTitle)
        val location: TextView = itemView.findViewById(R.id.eventLocation)
        val time: TextView = itemView.findViewById(R.id.eventTime)
        val editButton: ImageButton = itemView.findViewById(R.id.buttonEditEvent) // Fixed ID
        val deleteButton: ImageButton = itemView.findViewById(R.id.buttonDeleteEvent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.title.text = event.title
        holder.location.text = event.location
        holder.time.text = event.time

        holder.editButton.setOnClickListener {
            onEditClick(event)
        }

        holder.deleteButton.setOnClickListener {
            onDeleteClick(event)
        }
    }

    override fun getItemCount() = events.size
}