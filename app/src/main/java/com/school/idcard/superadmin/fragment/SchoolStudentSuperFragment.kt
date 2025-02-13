package com.school.idcard.superadmin.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.school.idcard.databinding.FragmentSchoolStudentSuperBinding
import com.school.idcard.network.AgentActionListener
import com.school.idcard.network.CommonResponse
import com.school.idcard.network.SharedPrefManager
import com.school.idcard.superadmin.adapter.StudentSuperAdminAdapter
import com.school.idcard.superadmin.model.StudentSuperAdmin
import com.school.idcard.superadmin.model.StudentSuperAdminModel
import com.scriza.scrizapay.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SchoolStudentSuperFragment : Fragment(),AgentActionListener {

    private var _binding: FragmentSchoolStudentSuperBinding? = null
    private val binding get() = _binding!!  // Safe access to binding
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var adapter: StudentSuperAdminAdapter
    private var schoolId: String? = null
    val studentList = ArrayList<StudentSuperAdmin>()

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
        _binding = FragmentSchoolStudentSuperBinding.inflate(inflater, container, false)
        sharedPrefManager = SharedPrefManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getStudentList()

        binding.rvSchoolStudent.layoutManager=LinearLayoutManager(context)
        adapter = StudentSuperAdminAdapter(requireContext(),studentList, this)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // ✅ Prevent memory leaks
    }

    companion object {
        fun newInstance(schoolId: String) = SchoolStudentSuperFragment().apply { // ✅ Corrected return type
            arguments = Bundle().apply {
                putString("schoolId", schoolId)
            }
        }
    }

    private fun getStudentList() {
        val token = "Bearer ${sharedPrefManager.getToken()}"

        if (schoolId.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Invalid school ID", Toast.LENGTH_SHORT).show()
            return
        }

        ApiClient.apiInterface.getSuperAdminStudentList(token, schoolId!!).enqueue(object : Callback<StudentSuperAdminModel> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<StudentSuperAdminModel>, response: Response<StudentSuperAdminModel>) {
                if (response.isSuccessful) {
                    val data = response.body()?.students
                    if (data != null) {
                        for (i in data){
                            studentList.add(StudentSuperAdmin(i.id,i.schoolid,i.schoolid,i.studentname,i.classno,i.mothername,i.fathername,i.contactno,i.section,i.gender,i.dateofbirth,i.address,i.admissiondate,i.details,i.photo,i.session,i.status,i.approvedTeacher,i.approvedAdmin,i.cardDesign,i.extrafieldsList))
                            binding.rvSchoolStudent.adapter = adapter
                            adapter.notifyDataSetChanged()
                        }
                        Toast.makeText(requireContext(), "Students fetched successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "No students found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<StudentSuperAdminModel>, t: Throwable) {
                Toast.makeText(requireContext(), "API Failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDeleteAgent(agentId: String) {
        TODO("Not yet implemented")
    }

    override fun onDeleteStaff(agentId: String) {
        val token="Bearer ${sharedPrefManager.getToken()}"
        ApiClient.apiInterface.deleteStudent(token.toString(), agentId).enqueue(object : Callback<CommonResponse>{
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if(response.isSuccessful){
                    Toast.makeText(requireContext(),response.body()!!.message,Toast.LENGTH_SHORT).show()
                    getStudentList()
                }else{
                    Toast.makeText(requireContext(),response.body()!!.message,Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Toast.makeText(requireContext(),t.message,Toast.LENGTH_SHORT).show()
            }

        })    }
}
