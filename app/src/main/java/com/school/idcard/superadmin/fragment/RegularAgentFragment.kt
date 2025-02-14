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
import com.school.idcard.databinding.FragmentRegularAgentBinding
import com.school.idcard.network.AgentActionListener
import com.school.idcard.network.CommonResponse
import com.school.idcard.network.SharedPrefManager
import com.school.idcard.superadmin.activity.AddAgentActivity
import com.school.idcard.superadmin.adapter.AgentListAdapter
import com.school.idcard.superadmin.model.Agent
import com.school.idcard.superadmin.model.AgentListModel
import com.school.idcard.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegularAgentFragment : Fragment(), AgentActionListener {
    private var _binding: FragmentRegularAgentBinding? = null
    private lateinit var sharedPrefManager: SharedPrefManager
    private val binding get() = _binding!!
    val agentList = ArrayList<Agent>()
    private lateinit var adapter:AgentListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegularAgentBinding.inflate(inflater, container, false)
        sharedPrefManager= SharedPrefManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvAgentList2.layoutManager= LinearLayoutManager(context)

        getAgentList("")


        adapter = AgentListAdapter(agentList,"Premium", requireContext(), this)

        binding.addAgentRegular.setOnClickListener {
            requireContext().startActivity(
                Intent(context, AddAgentActivity::class.java)
                .putExtra("type",0))
        }

        binding.filterBtnAgentR.setOnClickListener {
            val bottomSheet = StatusBottomSheetFragment { selectedStatus ->
                if (selectedStatus == "Active") {
                    getAgentList("true")
                } else {
                    getAgentList("false")
                }
            }
            bottomSheet.show(parentFragmentManager, "StatusBottomSheetFragment")
        }

    }

    private fun getAgentList(status:String) {
        val token="Bearer ${sharedPrefManager.getToken()}"
        ApiClient.apiInterface.getAgent(token,"").enqueue(object : Callback<AgentListModel> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<AgentListModel>,
                response: Response<AgentListModel>
            ) {
                if(response.isSuccessful){
                    agentList.clear()
                    val data = response.body()!!.agents
                    if(data!=null){
                        for(i in data){
                            if(i.agentType=="NORMAL"){
                                agentList.add(Agent(i.id,i.agentId,i.firstName,i.lastName,i.contactNo,i.address1,i.address2,i.agentEmail,i.status,i.city,i.pinCode,i.state,i.country,i.agentType,i.startSub,i.endSubs,i.subsStatus,i.noOfSchool,i.noOfStudent,i.noOfPendingCard,i.noOfSubmittedCard,i.noOfApprovedCard,i.noOfReceivePrintCard,i.noOfPrintProgressCard,i.noOfPrintedCard,i.noOfDeliveredCard))
                            }
                            binding.rvAgentList2.adapter = adapter
                            adapter.notifyDataSetChanged()
                        }
                    }else{
                        binding.noDataFound.visibility==View.VISIBLE
                    }
                }
            }

            override fun onFailure(call: Call<AgentListModel>, t: Throwable) {
                Toast.makeText(requireContext(),t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Prevent memory leaks
    }

    override fun onDeleteAgent(agentId: String) {
        val token="Bearer ${sharedPrefManager.getToken()}"
        ApiClient.apiInterface.deleteAgent(token.toString(), agentId).enqueue(object : Callback<CommonResponse>{
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if(response.isSuccessful){
                    Toast.makeText(requireContext(),response.body()!!.message,Toast.LENGTH_SHORT).show()
                    getAgentList("")
                }else{
                    Toast.makeText(requireContext(),response.body()!!.message,Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Toast.makeText(requireContext(),t.message,Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onDeleteStaff(agentId: String) {
        TODO("Not yet implemented")
    }

}