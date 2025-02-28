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

        calendarView = view.findViewById(R.id.calendarView)
        eventRecyclerView = view.findViewById(R.id.eventRecyclerView)
        addEventFab = view.findViewById(R.id.addEventFab)

        database = FirebaseDatabase.getInstance().getReference("Events")

        eventAdapter = EventAdapter(eventList)
        eventRecyclerView.adapter = eventAdapter
        eventRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Get selected date
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth-${month + 1}-$year"
            loadEvents(selectedDate)
            Log.d("ScheduleFragment", "Selected date: $selectedDate") // Add this log
            loadEvents(selectedDate)
        }

        addEventFab.setOnClickListener {
            val dialog = AddEventDialog(selectedDate) { event ->
                saveEventToFirebase(event) // Now `event` is of type `EventModel`
            }
            dialog.show(childFragmentManager, "AddEventDialog")
        }


        return view
    }

    private fun loadEvents(date: String) {
        database.orderByChild("date").equalTo(date).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                eventList.clear()
                for (eventSnapshot in snapshot.children) {
                    val event = eventSnapshot.getValue(EventModel::class.java)
                    event?.let { eventList.add(it) }
                }
                eventAdapter.notifyDataSetChanged() // Refresh the RecyclerView
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Failed to load events", error.toException())
            }
        })
    }
    override fun onResume() {
        super.onResume()
        loadEvents(selectedDate) // Reload events when returning to the fragment
    }


    private fun saveEventToFirebase(event: EventModel) {
        val eventId = database.push().key ?: return
        val eventWithId = event.copy(eventId = eventId) // ✅ Set eventId correctly

        database.child(eventId).setValue(eventWithId)
            .addOnSuccessListener { loadEvents(event.date) }
            .addOnFailureListener { Log.e("Firebase", "Failed to save event") }
    }

}
