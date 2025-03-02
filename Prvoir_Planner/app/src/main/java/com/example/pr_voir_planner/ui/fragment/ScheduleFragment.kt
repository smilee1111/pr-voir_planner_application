package com.example.pr_voir_planner.ui.fragment

import AddEventDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pr_voir_planner.R
import com.example.pr_voir_planner.adapter.EventAdapter
import com.example.pr_voir_planner.model.EventModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ScheduleFragment : Fragment() {

    private lateinit var calendarView: CalendarView
    private lateinit var eventRecyclerView: RecyclerView
    private lateinit var addEventFab: FloatingActionButton
    private lateinit var eventAdapter: EventAdapter
    private val eventList = mutableListOf<EventModel>()

    private lateinit var database: DatabaseReference
    private var selectedDate: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_schedule, container, false)

        // Initialize views
        calendarView = view.findViewById(R.id.calendarView)
        eventRecyclerView = view.findViewById(R.id.eventRecyclerView)
        addEventFab = view.findViewById(R.id.addEventFab)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().getReference("Events")

        // Setup RecyclerView with edit and delete callbacks
        eventAdapter = EventAdapter(eventList,
            onEditClick = { event ->
                editEvent(event)
            },
            onDeleteClick = { event ->
                deleteEvent(event)
            }
        )
        eventRecyclerView.adapter = eventAdapter
        eventRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Set initial selected date (today's date)
        selectedDate = getCurrentDate()
        loadEvents(selectedDate)

        // Handle date selection
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth-${month + 1}-$year"
            Log.d("ScheduleFragment", "Selected date: $selectedDate")
            loadEvents(selectedDate)
        }

        // Handle FAB click to add event
        addEventFab.setOnClickListener {
            val dialog = AddEventDialog(selectedDate, null) { event ->
                saveEventToFirebase(event)
            }
            dialog.show(childFragmentManager, "AddEventDialog")
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        loadEvents(selectedDate) // Reload events when fragment resumes
    }

    private fun getCurrentDate(): String {
        val calendar = java.util.Calendar.getInstance()
        val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)
        val month = calendar.get(java.util.Calendar.MONTH) + 1
        val year = calendar.get(java.util.Calendar.YEAR)
        return "$day-$month-$year"
    }

    private fun loadEvents(date: String) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserId == null) {
            Log.e("ScheduleFragment", "User not logged in")
            return
        }

        database.orderByChild("date").equalTo(date).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                eventList.clear()
                for (eventSnapshot in snapshot.children) {
                    val event = eventSnapshot.getValue(EventModel::class.java)
                    if (event != null && event.userId == currentUserId) {
                        eventList.add(event)
                    }
                }
                eventAdapter.notifyDataSetChanged() // Refresh RecyclerView
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Failed to load events: ${error.message}")
            }
        })
    }

    private fun saveEventToFirebase(event: EventModel) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserId == null) {
            Log.e("ScheduleFragment", "User not logged in")
            return
        }

        val eventId = database.push().key ?: return
        val eventWithId = event.copy(eventId = eventId, userId = currentUserId)

        database.child(eventId).setValue(eventWithId)
            .addOnSuccessListener {
                Log.d("Firebase", "Event saved successfully")
                loadEvents(event.date) // Reload events after saving
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Failed to save event: ${e.message}")
            }
    }

    private fun editEvent(event: EventModel) {
        val dialog = AddEventDialog(selectedDate, event) { updatedEvent ->
            updateEventInFirebase(updatedEvent)
        }
        dialog.show(childFragmentManager, "EditEventDialog")
    }

    private fun updateEventInFirebase(event: EventModel) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserId == null) {
            Log.e("ScheduleFragment", "User not logged in")
            return
        }

        if (event.eventId.isEmpty()) {
            Log.e("ScheduleFragment", "Event ID is missing")
            return
        }

        database.child(event.eventId).setValue(event)
            .addOnSuccessListener {
                Log.d("Firebase", "Event updated successfully")
                loadEvents(event.date) // Reload events after updating
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Failed to update event: ${e.message}")
            }
    }

    private fun deleteEvent(event: EventModel) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserId == null) {
            Log.e("ScheduleFragment", "User not logged in")
            return
        }

        if (event.eventId.isEmpty()) {
            Log.e("ScheduleFragment", "Event ID is missing")
            return
        }

        database.child(event.eventId).removeValue()
            .addOnSuccessListener {
                Log.d("Firebase", "Event deleted successfully")
                loadEvents(event.date) // Reload events after deletion
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Failed to delete event: ${e.message}")
            }
    }
}