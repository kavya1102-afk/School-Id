package com.school.idcard.superadmin.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.school.idcard.databinding.FragmentSuperAdminAgentBinding
import com.school.idcard.superadmin.activity.SuperAdminSideMenuActivity

class SuperAdminAgentFragment : Fragment() {

    private var _binding: FragmentSuperAdminAgentBinding? = null
    private val binding get() = _binding!!  // Safe access to binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSuperAdminAgentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TabAdapter(requireActivity()) // ✅ Use requireActivity()
        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 1

        binding.toolbar2.sideMenu.setOnClickListener { requireContext().startActivity(
            Intent(requireContext(), SuperAdminSideMenuActivity::class.java)
        ) }
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = if (position == 0) "Premium Agent" else "Regular Agent"
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Prevent memory leaks
    }
}

class TabAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PremiumAgentFragment()
            1 -> RegularAgentFragment()
            else -> PremiumAgentFragment()
        }
    }
}

