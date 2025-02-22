package com.school.idcard.superadmin.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.school.idcard.databinding.FragmentSuperadminProfileBinding
import com.school.idcard.network.SharedPrefManager
import com.school.idcard.other.ChangePasswordActivity
import com.school.idcard.other.LoginPage
import com.school.idcard.superadmin.SuperAdminDashboard
import com.school.idcard.superadmin.activity.AddAgentActivity
import com.school.idcard.superadmin.activity.AddSchoolActivity
import java.util.Calendar


class SuperadminProfileFragment : Fragment() {

    private var _binding: FragmentSuperadminProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPrefManager: SharedPrefManager// Safe access to binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        _binding = FragmentSuperadminProfileBinding.inflate(inflater, container, false)
        sharedPrefManager=SharedPrefManager(requireContext())
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


        binding.greetingTextView.text = greeting

        binding.homeSide.setOnClickListener {
            requireContext().startActivity(
                Intent(
                    requireContext(),
                    SuperAdminDashboard::class.java
                )
            )
        }

        binding.addAgentSide.setOnClickListener {
            requireContext().startActivity(
                Intent(
                    requireContext(),
                    AddAgentActivity::class.java
                )
            )
        }

        binding.addSchoolSide.setOnClickListener {
            requireContext(). startActivity(
                Intent(
                    requireContext(),
                    AddSchoolActivity::class.java
                )
            )
        }

        binding.changePasswordSide.setOnClickListener {
            requireContext().startActivity(
                Intent(
                    requireContext(),
                    ChangePasswordActivity::class.java
                )
            )
        }

        binding.logoutBtn.setOnClickListener {
            sharedPrefManager.clear()
            requireContext().startActivity(Intent(requireContext(), LoginPage::class.java))
        }

    }



    private fun logOut() {

        sharedPrefManager.clear()
        startActivity(Intent(requireContext(), LoginPage::class.java))
        requireActivity().finish()

    }




}