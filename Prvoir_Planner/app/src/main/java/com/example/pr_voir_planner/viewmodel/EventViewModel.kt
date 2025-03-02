package com.example.pr_voir_planner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pr_voir_planner.model.EventModel
import com.example.pr_voir_planner.repository.EventRepository

class EventViewModel(private val repo: EventRepository) : ViewModel() {

    fun addEvent(event: EventModel, callback: (Boolean, String) -> Unit) {
        repo.addEvent(event, callback)
    }

    fun getEventsForUser(userId: String, callback: (Boolean, List<EventModel>?, String) -> Unit) {
        repo.getEventsForUser(userId, callback)
    }

    fun updateEvent(event: EventModel, callback: (Boolean, String) -> Unit) {
        repo.updateEvent(event, callback)
    }

    fun deleteEvent(eventId: String, callback: (Boolean, String) -> Unit) {
        repo.deleteEvent(eventId, callback)
    }

    class EventViewModelFactory(private val repository: EventRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EventViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return EventViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
