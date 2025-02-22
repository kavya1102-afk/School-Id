package com.school.idcard.superadmin.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.school.idcard.databinding.FragmentSuperAdminHomeBinding
import com.school.idcard.superadmin.activity.SuperAdminSideMenuActivity
import com.school.idcard.superadmin.adapter.super_details_card_adapter
import com.school.idcard.superadmin.model.CardDetailsModel
import java.util.Calendar

class SuperAdminHomeFragment : Fragment() {
    private var _binding: FragmentSuperAdminHomeBinding? = null
    private val binding get() = _binding!!  // Safe access to binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSuperAdminHomeBinding.inflate(inflater, container, false)
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

        binding.profileCard.greetingTextView.text=greeting


        binding.profileCard.toolbar.sideMenu.setOnClickListener { requireContext().startActivity(
            Intent(requireContext(),SuperAdminSideMenuActivity::class.java)
        ) }


        binding.rvDetailsCard.layoutManager=
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)

        val cardList: MutableList<CardDetailsModel> = ArrayList()
        cardList.add(CardDetailsModel("Number of School","50"))
        cardList.add(CardDetailsModel("Number of Teacher","40"))
        cardList.add(CardDetailsModel("Number of Student","35"))
        cardList.add(CardDetailsModel("Number of Regular","28"))
        cardList.add(CardDetailsModel("Number of Premium","55"))

        val adapter = super_details_card_adapter(cardList)
        binding.rvDetailsCard.adapter = adapter

        val months = listOf("Current Month","Last Month","Last Year","Last Week")

        val adapter2 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, months)
        binding.spinnerMonth.adapter = adapter2
        binding.spinnerMonth.setSelection(0)
        binding.spinnerMonth2.adapter = adapter2
        binding.spinnerMonth2.setSelection(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Prevent memory leaks
    }

}