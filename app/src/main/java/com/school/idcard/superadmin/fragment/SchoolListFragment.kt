package com.school.idcard.superadmin.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.school.idcard.databinding.FragmentSchoolListBinding
import com.school.idcard.network.SharedPrefManager
import com.school.idcard.othermodel.School
import com.school.idcard.othermodel.SchoolResponse
import com.school.idcard.superadmin.activity.AddSchoolActivity
import com.school.idcard.superadmin.activity.SubmittedForPrintScreen
import com.school.idcard.superadmin.activity.SuperAdminSideMenuActivity
import com.school.idcard.superadmin.adapter.SchoolListAdapter
import com.scriza.scrizapay.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SchoolListFragment : Fragment() {

    private var _binding: FragmentSchoolListBinding? = null
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var adapter: SchoolListAdapter
    private val binding get() = _binding!!  // Safe access to binding
    val schoolList = ArrayList<School>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSchoolListBinding.inflate(inflater, container, false)
        sharedPrefManager = SharedPrefManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSchoolList("")

        binding.rvSchoolList.layoutManager = LinearLayoutManager(context)
        adapter = SchoolListAdapter(schoolList, "Premium", requireContext())

        binding.addSchool.setOnClickListener {
            requireContext().startActivity(
                Intent(context, AddSchoolActivity::class.java)
                    .putExtra("type", 0)
            )
        }

        binding.toolbar.sideMenu.setOnClickListener { requireContext().startActivity(
            Intent(requireContext(), SuperAdminSideMenuActivity::class.java)
        ) }

        binding.filterBtnSchool.setOnClickListener {
            val bottomSheet = StatusBottomSheetFragment { selectedStatus ->
                getSchoolList(selectedStatus)
            }
            bottomSheet.show(parentFragmentManager, "StatusBottomSheetFragment")
        }

        binding.submittedPrint.setOnClickListener {
            requireContext().startActivity(Intent(requireContext(),SubmittedForPrintScreen::class.java))
        }

    }

    private fun getSchoolList(selectedStatus:String) {
        val token = "Bearer ${sharedPrefManager.getToken()}"
        ApiClient.apiInterface.getSchool(token, selectedStatus).enqueue(object : Callback<SchoolResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<SchoolResponse>,
                response: Response<SchoolResponse>
            ) {
                if (response.isSuccessful) {
                    schoolList.clear()
                    val data = response.body()!!.schools
                   if(data==null){
                       binding.noDataFound.visibility==View.VISIBLE
                   }else{
                       for (i in data) {
                           schoolList.add(School(i.id,i.schoolId,i.schoolName,i.contactNo,i.address1,i.address2,i.city,i.pinCode,i.state,i.country,i.schoolEmail,i.principalName,i.agentId,i.status,i.createDate,i.idCardDesign))
                       }
                       binding.rvSchoolList.adapter = adapter
                       adapter.notifyDataSetChanged()
                   }
                }
            }

            override fun onFailure(call: Call<SchoolResponse>, t: Throwable) {
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Prevent memory leaks
    }


}