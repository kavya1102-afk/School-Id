package com.school.idcard.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.school.idcard.databinding.BottomSheetAddRoleBinding

class AddRoleBottomSheet(private val onRoleAdded: (String) -> Unit) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetAddRoleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAddRoleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivClose.setOnClickListener {
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnAddRole.setOnClickListener {
            val role = binding.etRole.textInputLayout.editText!!.text.toString().trim()
            if (role.isNotEmpty()) {
                onRoleAdded(role) // Send role data back to the activity
                dismiss()
            } else {
                binding.etRole.textInputLayout.error = "Enter a valid role"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks
    }
}
