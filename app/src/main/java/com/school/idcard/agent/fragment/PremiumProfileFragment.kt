package com.school.idcard.agent.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.school.idcard.agent.EditProfileActivity
import com.school.idcard.databinding.FragmentPremiumProfileBinding
import com.school.idcard.network.ApiClient
import com.school.idcard.network.SharedPrefManager
import com.school.idcard.othermodel.ProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PremiumProfileFragment : Fragment() {

    private var _binding: FragmentPremiumProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPrefManager: SharedPrefManager// Safe access to binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPremiumProfileBinding.inflate(inflater, container, false)
        sharedPrefManager= SharedPrefManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.toolbar.toolbar.sideMenu.visibility=View.GONE

        binding.editProfile.setOnClickListener {
            requireContext().startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }

        getProfile()

    }

    private fun getProfile() {
        val token="Bearer ${sharedPrefManager.getToken()}"

        ApiClient.apiInterface.getProfile(token).enqueue(object : Callback<ProfileResponse>{
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {
                if(response.isSuccessful){
                    if(response.body()!!.profile!=null){
                        binding.agentId.text=response.body()!!.profile!!.agentId
                        binding.agentName.text="${response.body()!!.profile!!.firstName} ${response.body()!!.profile!!.lastName}"
                        binding.contactAgent.text="+91-${response.body()!!.profile!!.contactNo}"
                        binding.agentAddress.text="${response.body()!!.profile!!.address1}"
                        binding.agentCity.text="${response.body()!!.profile!!.city}"
                        binding.agentState.text="${response.body()!!.profile!!.state}"
                        binding.agentEmail.text="${response.body()!!.profile!!.agentEmail}"
                        binding.agentPincode.text="${response.body()!!.profile!!.pinCode}"

                        if(response.body()!!.profile!!.status == true){
                            binding.statusAgent.text="Active"
                            binding.statusAgent.setTextColor(Color.GREEN)
                        }else{
                            binding.statusAgent.text="In-Active"
                            binding.statusAgent.setTextColor(Color.RED)
                        }


                    }else{
                        Toast.makeText(requireContext(),response.body()!!.message,Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(requireContext(),response.message(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }

}