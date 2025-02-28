import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.example.pr_voir_planner.databinding.FragmentAddEventDialogBinding
import com.example.pr_voir_planner.model.EventModel
import com.google.firebase.auth.FirebaseAuth

class AddEventDialog(private val selectedDate: String, private val onEventAdded: (EventModel) -> Unit) : DialogFragment() {

    private lateinit var binding: FragmentAddEventDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using the generated binding class
        binding = FragmentAddEventDialogBinding.inflate(inflater, container, false)

        // Bind UI components
        val titleEditText = binding.eventTitle
        val locationEditText = binding.eventLocation
        val timePicker = binding.eventTime

        // Set save button listener
        binding.saveButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val location = locationEditText.text.toString()
            val time = "${timePicker.hour}:${timePicker.minute}"

            // Create EventModel object
            val event = EventModel(
                title = title,
                location = location,
                date = selectedDate,
                time = time,
                userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            )

            // Return the event via the callback
            onEventAdded(event)
            dismiss() // Close the dialog
        }

        return binding.root
    }
}
