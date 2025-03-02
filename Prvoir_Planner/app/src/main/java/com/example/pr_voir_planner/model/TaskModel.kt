package com.example.pr_voir_planner.model

import android.os.Parcel
import android.os.Parcelable

data class TaskModel(
    var taskId: String = "",
    var title: String = "",
    var description: String = "",
    var date: String = "", // New field for date
    var time: String = "", // New field for time
    var status: String = "To-Do", // Default status
    var priority: String = "Medium", // Default priority
    var userId: String = "" // Reference to the user who created the task
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "To-Do",
        parcel.readString() ?: "Medium",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(taskId)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(date)
        parcel.writeString(time)
        parcel.writeString(status)
        parcel.writeString(priority)
        parcel.writeString(userId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TaskModel> {
        override fun createFromParcel(parcel: Parcel): TaskModel {
            return TaskModel(parcel)
        }

        override fun newArray(size: Int): Array<TaskModel?> {
            return arrayOfNulls(size)
        }
    }
}