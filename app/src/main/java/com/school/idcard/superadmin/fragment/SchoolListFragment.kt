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
import com.school.idcard.network.ApiClient
import com.school.idcard.network.CommonResponse
import com.school.idcard.network.SchoolActionListener
import com.school.idcard.network.SharedPrefManager
import com.school.idcard.othermodel.School
import com.school.idcard.othermodel.SchoolResponse
import com.school.idcard.superadmin.activity.AddSchoolActivity
import com.school.idcard.superadmin.activity.SubmittedForPrintScreen
import com.school.idcard.superadmin.adapter.SchoolListAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SchoolListFragment : Fragment(), SchoolActionListener {

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
        binding.loaderLayout.visibility = View.VISIBLE

        binding.rvSchoolList.layoutManager = LinearLayoutManager(context)
        adapter = SchoolListAdapter(schoolList, "Premium", requireContext(), this)

        binding.addSchool.setOnClickListener {
            requireContext().startActivity(
                Intent(context, AddSchoolActivity::class.java)
                    .putExtra("type", 0)
            )
        }

        binding.toolbar.sideMenu.visibility=View.GONE

        binding.filterBtnSchool.setOnClickListener {
            val bottomSheet = StatusBottomSheetFragment { selectedStatus ->
                getSchoolList(selectedStatus)
            }
            bottomSheet.show(parentFragmentManager, "StatusBottomSheetFragment")
        }

        binding.submittedPrint.setOnClickListener {
            requireContext().startActivity(Intent(requireContext(), SubmittedForPrintScreen::class.java))
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
                    binding.loaderLayout.visibility = View.GONE
                    binding.lottieProgress.pauseAnimation()
                    schoolList.clear()
                    val data = response.body()!!.schools
                   if(data==null){
                       binding.noDataFound.visibility==View.VISIBLE
                   }else{
                       for (i in data) {
                           schoolList.add(School(i.id,i.schoolId,i.schoolName,i.contactNo,i.address1,i.address2,i.city,i.pinCode,i.state,i.country,i.schoolEmail,i.principalName,i.agentId,i.agentName,i.status,i.createDate,i.idCardDesign))
                       }
                       binding.rvSchoolList.adapter = adapter
                       adapter.notifyDataSetChanged()
                   }
                }
            }

            override fun onFailure(call: Call<SchoolResponse>, t: Throwable) {
                binding.loaderLayout.visibility = View.GONE
                binding.lottieProgress.pauseAnimation()
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Prevent memory leaks
    }

    override fun onDeleteSchool(schoolId: String) {
        val token="Bearer ${sharedPrefManager.getToken()}"
        ApiClient.apiInterface.deleteSchool(token.toString(), schoolId).enqueue(object : Callback<CommonResponse>{
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if(response.isSuccessful){
                    Toast.makeText(requireContext(),response.body()!!.message,Toast.LENGTH_SHORT).show()
                    getSchoolList("")
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