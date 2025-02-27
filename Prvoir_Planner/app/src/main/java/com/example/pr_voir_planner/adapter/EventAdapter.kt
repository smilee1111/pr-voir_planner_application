//package com.example.pr_voir_planner.ui.fragment
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.example.pr_voir_planner.R
//import kotlinx.android.synthetic.main.item_event.view.*
//
//class EventAdapter(private val events: List<String>) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_eventpage, parent, false)
//        return EventViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
//        holder.bind(events[position])
//    }
//
//    override fun getItemCount(): Int = events.size
//
//    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        fun bind(event: String) {
//            itemView.tvEventTitle.text = event
//        }
//    }
//}
