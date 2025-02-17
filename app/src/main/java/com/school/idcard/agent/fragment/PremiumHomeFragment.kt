package com.school.idcard.agent.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.school.idcard.agent.PremiumAgentSideMenuActivity
import com.school.idcard.databinding.FragmentPremiumHomeBinding
import com.school.idcard.superadmin.adapter.super_details_card_adapter
import com.school.idcard.superadmin.model.CardDetailsModel

class PremiumHomeFragment : Fragment() {

    private var _binding: FragmentPremiumHomeBinding? = null
    private val binding get() = _binding!!  // Safe access to binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPremiumHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileCard.toolbar.sideMenu.setOnClickListener { requireContext().startActivity(
            Intent(requireContext(), PremiumAgentSideMenuActivity::class.java)
        ) }


        binding.rvDetailsCard.layoutManager=
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)

        val cardList: MutableList<CardDetailsModel> = ArrayList()
        cardList.add(CardDetailsModel("Number of School","50"))
        cardList.add(CardDetailsModel("Number of Teacher","40"))
        cardList.add(CardDetailsModel("Number of Student","35"))

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