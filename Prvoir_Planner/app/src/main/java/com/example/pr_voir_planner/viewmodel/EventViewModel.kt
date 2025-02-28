package com.example.pr_voir_planner.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pr_voir_planner.model.EventModel
import com.example.pr_voir_planner.repository.EventRepository

class EventViewModel(private val repo: EventRepository) : ViewModel() {

    fun addEvent(event: EventModel, callback: (Boolean, String) -> Unit) {
        repo.addEvent(event, callback)
    }

    fun getEventsForUser(userId: String, callback: (Boolean, List<EventModel>?, String) -> Unit) {
        repo.getEventsForUser(userId, callback)
    }

    fun deleteEvent(eventId: String, callback: (Boolean, String) -> Unit) {
        repo.deleteEvent(eventId, callback)
    }
}
