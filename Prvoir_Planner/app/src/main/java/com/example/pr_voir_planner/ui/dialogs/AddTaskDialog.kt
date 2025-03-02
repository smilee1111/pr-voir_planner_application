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
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.pr_voir_planner.R
import com.example.pr_voir_planner.model.TaskModel
import com.google.android.material.button.MaterialButton
import java.util.*

class AddTaskDialog(
    private val selectedDate: String,
    private val taskToEdit: TaskModel? = null, // Optional parameter for editing
    private val onTaskSubmitted: (TaskModel) -> Unit // Changed name to reflect both add/edit
) : DialogFragment() {

    private var selectedTime: String? = null // Nullable to handle uninitialized state

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_task, null)

        // Find views from the updated layout
        val titleInput: EditText = view.findViewById(R.id.taskTitleInput)
        val descriptionInput: EditText = view.findViewById(R.id.taskDescriptionInput)
        val prioritySpinner: Spinner = view.findViewById(R.id.prioritySpinner)
        val statusSpinner: Spinner = view.findViewById(R.id.statusSpinner)
        val dueDateTextView: TextView = view.findViewById(R.id.dueDateTextView)
        val saveButton: MaterialButton = view.findViewById(R.id.saveTaskButton)

        // Set up spinners for priority and status
        val priorityAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.task_priority_array,
            android.R.layout.simple_spinner_item
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        prioritySpinner.adapter = priorityAdapter

        val statusAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.task_status_array,
            android.R.layout.simple_spinner_item
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        statusSpinner.adapter = statusAdapter

        // Pre-fill fields if editing
        taskToEdit?.let { task ->
            titleInput.setText(task.title)
            descriptionInput.setText(task.description)
            selectedTime = task.time
            dueDateTextView.text = "Date: ${task.date}\nTime: ${task.time}"
            // Set spinner selections
            val priorityPosition = priorityAdapter.getPosition(task.priority)
            prioritySpinner.setSelection(priorityPosition)
            val statusPosition = statusAdapter.getPosition(task.status)
            statusSpinner.setSelection(statusPosition)
        } ?: run {
            // Initialize due date display for adding new task
            dueDateTextView.text = "Date: $selectedDate\nTap to select time"
        }

        // Time Picker functionality
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        dueDateTextView.setOnClickListener {
            TimePickerDialog(
                requireContext(),
                { _, selectedHour, selectedMinute ->
                    selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                    dueDateTextView.text = "Date: $selectedDate\nTime: $selectedTime"
                },
                hour,
                minute,
                false
            ).show()
        }

        // Handle Save button click
        saveButton.setOnClickListener {
            // Validate inputs
            val title = titleInput.text.toString().trim()
            val description = descriptionInput.text.toString().trim()

            when {
                title.isEmpty() -> {
                    Toast.makeText(requireContext(), "Please enter a task title", Toast.LENGTH_SHORT).show()
                }
                selectedTime == null -> {
                    Toast.makeText(requireContext(), "Please select a time for the task", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val task = TaskModel(
                        taskId = taskToEdit?.taskId ?: "", // Preserve original ID if editing
                        userId = taskToEdit?.userId ?: "", // Preserve userId if editing
                        title = title,
                        description = description,
                        date = selectedDate,
                        time = selectedTime!!, // Safe to use !! since we checked for null
                        status = statusSpinner.selectedItem.toString(),
                        priority = prioritySpinner.selectedItem.toString()
                    )
                    onTaskSubmitted(task)
                    dismiss() // Close the dialog on success
                }
            }
        }

        // Build the dialog
        builder.setView(view)
            .setTitle(if (taskToEdit != null) "Edit Task" else "Add Task") // Dynamic title
            .setNegativeButton("Cancel") { _, _ -> dismiss() }

        return builder.create().apply {
            setOnShowListener {
                getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.primary)
                )
            }
        }
    }
}