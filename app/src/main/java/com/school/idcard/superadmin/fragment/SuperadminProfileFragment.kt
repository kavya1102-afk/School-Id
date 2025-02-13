package com.school.idcard.superadmin.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.school.idcard.databinding.FragmentSuperadminProfileBinding
import com.school.idcard.network.SharedPrefManager
import com.school.idcard.other.LoginPage
import com.school.idcard.superadmin.activity.SuperAdminSideMenuActivity


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

//        binding.logoutBtn.setOnClickListener {
//
//        }

        binding.toolbar.toolbar.sideMenu.setOnClickListener { requireContext().startActivity(
            Intent(requireContext(), SuperAdminSideMenuActivity::class.java)
        ) }

    }

    private fun logOut() {

        sharedPrefManager.clear()
        startActivity(Intent(requireContext(), LoginPage::class.java))
        requireActivity().finish()

    }




}