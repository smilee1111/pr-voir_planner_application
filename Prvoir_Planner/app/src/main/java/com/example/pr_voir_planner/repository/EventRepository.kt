package com.example.pr_voir_planner.repository

import com.example.pr_voir_planner.model.EventModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

interface EventRepository {
    fun addEvent(event: EventModel, callback: (Boolean, String) -> Unit)
    fun getEventsForUser(userId: String, callback: (Boolean, List<EventModel>?, String) -> Unit)
    fun deleteEvent(eventId: String, callback: (Boolean, String) -> Unit)
    fun updateEvent(event: EventModel, callback: (Boolean, String) -> Unit) // Added
}
