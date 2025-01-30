package com.example.pr_voir_planner.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.pr_voir_planner.R
import com.example.pr_voir_planner.databinding.ActivityNavigationBinding
import com.example.pr_voir_planner.ui.fragment.HomeFragment
import com.example.pr_voir_planner.ui.fragment.ProfileFragment
import com.example.pr_voir_planner.ui.fragment.ScheduleFragment
import com.example.pr_voir_planner.ui.fragment.TaskFragment

class NavigationActivity : AppCompatActivity() {
    lateinit var binding: ActivityNavigationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())
        binding.bottomView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navHome -> replaceFragment(HomeFragment())
                R.id.navSchedule -> replaceFragment(ScheduleFragment())
                R.id.navTask -> replaceFragment(TaskFragment())
                R.id.navProfile -> replaceFragment(ProfileFragment())
                else -> {}
            }
            true
        }

        setContentView(R.layout.activity_navigation)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager =
            supportFragmentManager
        val fragmentTransaction: FragmentTransaction =
            fragmentManager.beginTransaction()
        fragmentTransaction.replace(
            binding.frameBottom.id,
            fragment
        )
        fragmentTransaction.commit()
    }
}