package com.school.idcard.admin.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.school.idcard.admin.AddStaffActivity
import com.school.idcard.admin.AdminSideMenuActivity
import com.school.idcard.databinding.FragmentAdminStaffBinding

class AdminStaffFragment : Fragment() {

    private var _binding: FragmentAdminStaffBinding? = null
    private val binding get() = _binding!!  // Safe access to binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminStaffBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.sideMenu.setOnClickListener {
            requireContext().startActivity(Intent(requireContext(),AdminSideMenuActivity::class.java))
        }

        binding.addStaffBtn.setOnClickListener {
            requireContext().startActivity(Intent(requireContext(),AddStaffActivity::class.java))
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Prevent memory leaks
    }


}