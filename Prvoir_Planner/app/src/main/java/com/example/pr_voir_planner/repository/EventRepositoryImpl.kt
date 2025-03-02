package com.example.pr_voir_planner.repository

import com.example.pr_voir_planner.model.EventModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EventRepositoryImpl : EventRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun addEvent(event: EventModel, callback: (Boolean, String) -> Unit) {
        val eventId = db.collection("events").document().id
        event.eventId = eventId

        db.collection("events").document(eventId)
            .set(event)
            .addOnSuccessListener {
                callback(true, "Event added successfully")
            }
            .addOnFailureListener { e ->
                callback(false, e.message ?: "Failed to add event")
            }
    }

    override fun getEventsForUser(userId: String, callback: (Boolean, List<EventModel>?, String) -> Unit) {
        db.collection("events")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                val events = documents.mapNotNull { it.toObject(EventModel::class.java) }
                callback(true, events, "Events fetched successfully")
            }
            .addOnFailureListener { e ->
                callback(false, null, e.message ?: "Failed to fetch events")
            }
    }

    override fun deleteEvent(eventId: String, callback: (Boolean, String) -> Unit) {
        db.collection("events").document(eventId)
            .delete()
            .addOnSuccessListener {
                callback(true, "Event deleted successfully")
            }
            .addOnFailureListener { e ->
                callback(false, e.message ?: "Failed to delete event")
            }
    }
    override fun updateEvent(event: EventModel, callback: (Boolean, String) -> Unit) {
        val eventId = event.eventId ?: return callback(false, "Event ID is missing")
        val userId = auth.currentUser?.uid ?: return callback(false, "User not authenticated")

        // Ensure userId matches the current user for security
        if (event.userId != userId) {
            return callback(false, "Unauthorized to update this event")
        }

        db.collection("events").document(eventId)
            .set(event) // Overwrites the existing document
            .addOnSuccessListener {
                callback(true, "Event updated successfully")
            }
            .addOnFailureListener { e ->
                callback(false, e.message ?: "Failed to update event")
            }
    }
}
