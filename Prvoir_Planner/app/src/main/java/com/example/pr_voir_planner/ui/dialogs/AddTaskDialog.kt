package com.example.pr_voir_planner.ui.dialog

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.pr_voir_planner.R
import com.example.pr_voir_planner.model.TaskModel
import java.util.*

class AddTaskDialog(private val selectedDate: String, private val onTaskAdded: (TaskModel) -> Unit) : DialogFragment() {

    private lateinit var selectedTime: String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = android.app.AlertDialog.Builder(requireContext())
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_task, null)

        val titleInput: EditText = view.findViewById(R.id.taskTitleInput)
        val descriptionInput: EditText = view.findViewById(R.id.taskDescriptionInput)
        val prioritySpinner: Spinner = view.findViewById(R.id.prioritySpinner)
        val statusSpinner: Spinner = view.findViewById(R.id.statusSpinner)
        val dueDateTextView: TextView = view.findViewById(R.id.dueDateTextView)

        // Set up spinners for priority and status
        val priorityAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.task_priority_array,
            android.R.layout.simple_spinner_item
        )
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        prioritySpinner.adapter = priorityAdapter

        val statusAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.task_status_array,
            android.R.layout.simple_spinner_item
        )
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        statusSpinner.adapter = statusAdapter

        // Display the selected date
        dueDateTextView.text = "Date: $selectedDate\nTap to select time"

        // Time Picker functionality
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        // When user clicks on the date, show TimePickerDialog
        dueDateTextView.setOnClickListener {
            TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
                // Format the selected time
                selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)

                // Update the TextView to show selected date and time
                dueDateTextView.text = "Date: $selectedDate\nTime: $selectedTime"
            }, hour, minute, false).show()
        }

        builder.setView(view)
            .setPositiveButton("Save") { _, _ ->
                // Check if selectedTime is initialized
                if (!::selectedTime.isInitialized) {
                    Toast.makeText(requireContext(), "Please select a time for the task", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val task = TaskModel(
                    title = titleInput.text.toString(),
                    description = descriptionInput.text.toString(),
                    date = selectedDate, // Ensure the selected date is passed
                    time = selectedTime, // Ensure the selected time is passed
                    status = statusSpinner.selectedItem.toString(),
                    priority = prioritySpinner.selectedItem.toString()
                )
                onTaskAdded(task)
            }
            .setNegativeButton("Cancel", null)

        return builder.create()
    }
}