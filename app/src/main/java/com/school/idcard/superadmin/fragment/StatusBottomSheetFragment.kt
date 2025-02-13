package com.school.idcard.superadmin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.school.idcard.R
import com.school.idcard.databinding.FilterLayoutBinding

class StatusBottomSheetFragment(private val onStatusSelected: (String) -> Unit) : BottomSheetDialogFragment() {

    private var _binding: FilterLayoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var statusList: Array<String>
    private var finalStatus:String?="Active"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FilterLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val statusArray = resources.getStringArray(R.array.status_list)
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item_file, statusArray)

        binding.autoCompleteTextView2.setAdapter(adapter)

        binding.autoCompleteTextView2.setOnItemClickListener { _, _, position, _ ->
            finalStatus = statusArray[position]  // Fix: Use statusArray instead of statusList
        }

        binding.applyFilterBtn.setOnClickListener {
            onStatusSelected(finalStatus.toString())
            dismiss() // Close the bottom sheet
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
