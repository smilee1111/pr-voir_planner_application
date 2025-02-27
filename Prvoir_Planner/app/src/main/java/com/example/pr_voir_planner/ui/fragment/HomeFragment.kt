package com.example.pr_voir_planner.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pr_voir_planner.R
import com.example.pr_voir_planner.databinding.FragmentHomeBinding
import com.example.pr_voir_planner.ui.adapter.EventAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Example values (fetch these from database/API later)
        binding.tvToDo.text = "To-Do: 5"
        binding.tvDoing.text = "Doing: 3"
        binding.tvDone.text = "Done: 2"

        // Set up RecyclerView
        binding.rvEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEvents.adapter = EventAdapter(getDummyEvents())

        // FAB Click Action
        binding.fabAdd.setOnClickListener {
            // Open Add Task/Event Screen
        }
    }

    private fun getDummyEvents(): List<String> {
        return listOf("Meeting at 10 AM", "Project Deadline at 5 PM", "Gym at 7 PM")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
