package com.example.pr_voir_planner.model

import android.os.Parcel
import android.os.Parcelable

data class EventModel(
    var eventId: String = "",
    var title: String = "",
    var location: String = "",
    var date: String = "",
    var time: String = "",
    var userId: String = "" // Reference to the user who created the event
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(eventId)
        parcel.writeString(title)
        parcel.writeString(location)
        parcel.writeString(date)
        parcel.writeString(time)
        parcel.writeString(userId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EventModel> {
        override fun createFromParcel(parcel: Parcel): EventModel {
            return EventModel(parcel)
        }

        override fun newArray(size: Int): Array<EventModel?> {
            return arrayOfNulls(size)
        }
    }
}
