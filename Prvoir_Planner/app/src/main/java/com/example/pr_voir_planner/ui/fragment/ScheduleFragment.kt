//package com.example.pr_voir_planner.ui.fragment
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.CalendarView
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.pr_voir_planner.R
//import com.google.android.material.floatingactionbutton.FloatingActionButton
//import java.text.SimpleDateFormat
//import java.util.*
//
//class ScheduleFragment : Fragment() {
//
//    private lateinit var calendarView: CalendarView
//    private lateinit var fabAddEvent: FloatingActionButton
//    private lateinit var recyclerViewEvents: RecyclerView
//
//    private val events = mutableListOf<String>() // Store event titles for simplicity.
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        val rootView = inflater.inflate(R.layout.fragment_schedule, container, false)
//
//        calendarView = rootView.findViewById(R.id.calendarView)
//        fabAddEvent = rootView.findViewById(R.id.fab_add_event)
//        recyclerViewEvents = rootView.findViewById(R.id.recyclerViewEvents)
//
//        // Setup RecyclerView
//        recyclerViewEvents.layoutManager = LinearLayoutManager(context)
//        val adapter = EventAdapter(events)
//        recyclerViewEvents.adapter = adapter
//
//        // Handle calendar date change
//        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
//            val selectedDate = "$dayOfMonth/${month + 1}/$year"
//            // Load events for the selected date (for simplicity, we just display the selected date)
//            events.clear()
//            events.add("Event for $selectedDate") // Replace with actual event loading logic
//            adapter.notifyDataSetChanged()
//        }
//
//        // Handle floating action button click to add new event
//        fabAddEvent.setOnClickListener {
//            showAddEventDialog()
//        }
//
//        return rootView
//    }
//
//    private fun showAddEventDialog() {
//        // Implement a dialog to add event details
//        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//        val selectedDate = dateFormat.format(Date(calendarView.date)) // Get selected date
//        Toast.makeText(context, "Add event for $selectedDate", Toast.LENGTH_SHORT).show()
//
//        // Here, you can open a dialog to input event details (title, description, etc.)
//    }
//}
