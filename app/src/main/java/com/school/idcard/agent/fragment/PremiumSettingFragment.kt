package com.school.idcard.agent.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.school.idcard.agent.PremiumAgentDashboard
import com.school.idcard.databinding.FragmentPremiumSettingBinding
import com.school.idcard.network.SharedPrefManager
import com.school.idcard.other.ChangePasswordActivity
import com.school.idcard.other.LoginPage
import com.school.idcard.superadmin.activity.AddSchoolActivity
import java.util.Calendar

class PremiumSettingFragment : Fragment() {

    private var _binding: FragmentPremiumSettingBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPrefManager: SharedPrefManager// Safe access to binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPremiumSettingBinding.inflate(inflater, container, false)
        sharedPrefManager= SharedPrefManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        val greeting = when (hour) {
            in 0..11 -> "Good Morning 🌅"
            in 12..15 -> "Good Afternoon ☀️"
            else -> "Good Evening 🌙"
        }

        binding.greetingTextView.text=greeting



        binding.homeSide.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    PremiumAgentDashboard::class.java
                )
            )
        }

        binding.addSchoolSide.setOnClickListener {
            startActivity(
                Intent(requireContext(),AddSchoolActivity::class.java)
            )
        }

        binding.changePasswordSide.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    ChangePasswordActivity::class.java
                )
            )
        }

        binding.logoutBtn.setOnClickListener {
            sharedPrefManager.clear()
            startActivity(Intent(requireContext(), LoginPage::class.java))
        }


    }

}