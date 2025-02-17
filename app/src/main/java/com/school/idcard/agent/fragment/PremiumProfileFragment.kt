package com.school.idcard.agent.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.school.idcard.databinding.FragmentSuperadminProfileBinding
import com.school.idcard.network.SharedPrefManager

class PremiumProfileFragment : Fragment() {

    private var _binding: FragmentSuperadminProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPrefManager: SharedPrefManager// Safe access to binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSuperadminProfileBinding.inflate(inflater, container, false)
        sharedPrefManager= SharedPrefManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.toolbar.toolbar.sideMenu.visibility=View.GONE

    }

}