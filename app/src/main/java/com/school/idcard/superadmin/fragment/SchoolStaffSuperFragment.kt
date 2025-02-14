package com.school.idcard.superadmin.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.school.idcard.databinding.FragmentSchoolStaffSuperBinding
import com.school.idcard.network.AgentActionListener
import com.school.idcard.network.CommonResponse
import com.school.idcard.network.SharedPrefManager
import com.school.idcard.superadmin.adapter.StaffSuperAdminAdapter
import com.school.idcard.superadmin.model.Staff
import com.school.idcard.superadmin.model.StaffResponse
import com.school.idcard.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SchoolStaffSuperFragment : Fragment(), AgentActionListener {
    private var _binding: FragmentSchoolStaffSuperBinding? = null
    private val binding get() = _binding!!  // Safe access to binding
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var adapter: StaffSuperAdminAdapter
    private var schoolId: String? = null
    val staffList = ArrayList<Staff>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            schoolId = it.getString("schoolId") // ✅ Retrieve schoolId from arguments
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSchoolStaffSuperBinding.inflate(inflater, container, false)
        sharedPrefManager = SharedPrefManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getStaffList()

        binding.rvSchoolStaff.layoutManager= LinearLayoutManager(context)
        adapter = StaffSuperAdminAdapter(requireContext(),staffList, this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // ✅ Prevent memory leaks
    }

    companion object {
        fun newInstance(schoolId: String) = SchoolStaffSuperFragment().apply { // ✅ Corrected return type
            arguments = Bundle().apply {
                putString("schoolId", schoolId)
            }
        }
    }

    private fun getStaffList() {
        val token = "Bearer ${sharedPrefManager.getToken()}"

        if (schoolId.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Invalid school ID", Toast.LENGTH_SHORT).show()
            return
        }

        ApiClient.apiInterface.getSuperAdminStaffList(token, schoolId!!).enqueue(object :
            Callback<StaffResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<StaffResponse>, response: Response<StaffResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()?.staffs
                    if (data != null) {
                        staffList.clear()
                        staffList.addAll(data)
                        binding.rvSchoolStaff.adapter = adapter
                        adapter.notifyDataSetChanged()
                        Toast.makeText(requireContext(), "Staff fetched successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        binding.noDataFound.visibility==View.VISIBLE
                        Toast.makeText(requireContext(), "No Staff found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<StaffResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "API Failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDeleteAgent(agentId: String) {
        TODO("Not yet implemented")
    }

    override fun onDeleteStaff(agentId: String) {
        val token = "Bearer ${sharedPrefManager.getToken()}"
        ApiClient.apiInterface.deleteStaff(token.toString(), agentId).enqueue(object : Callback<CommonResponse>{
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if(response.isSuccessful){
                    Toast.makeText(requireContext(),response.body()!!.message,Toast.LENGTH_SHORT).show()
                    getStaffList()
                }else{
                    Toast.makeText(requireContext(),response.body()!!.message,Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Toast.makeText(requireContext(),t.message,Toast.LENGTH_SHORT).show()
            }

        })
    }
}