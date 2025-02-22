package com.school.idcard.agent

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.school.idcard.R
import com.school.idcard.databinding.ActivityEditProfileBinding
import com.school.idcard.network.ApiClient
import com.school.idcard.network.CommonResponse
import com.school.idcard.network.SharedPrefManager
import com.school.idcard.othermodel.ProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding:ActivityEditProfileBinding
    private lateinit var sharedPrefManager: SharedPrefManager

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        sharedPrefManager= SharedPrefManager(this)
        binding.toolbar.fileName.text="Edit Profile"
        binding.toolbar.backArrowBtn.setOnClickListener { finish() }

        binding.agentId.textInputLayout.isEnabled = false
        binding.addAgentEmail.textInputLayout.isEnabled = false

        getProfile()

        binding.agentId.textInputLayout.hint="User Id"
        binding.addAgentFirstName.textInputLayout.hint="First Name"
        binding.addAgentLastName.textInputLayout.hint="Last Name"
        binding.addAgentEmail.textInputLayout.hint="Email Id"
        binding.addAgentContact.textInputLayout.hint="Contact No"
        binding.addAgentPincode.textInputLayout.hint="Pincode"
        binding.addAgentState.textInputLayout.hint="State"
        binding.addAgentCity.textInputLayout.hint="City"
        binding.addAgentCountry.textInputLayout.hint="Country"
        binding.addAgentAddress.textInputLayout.hint="Address"

        // Setting Input Filters
        binding.addAgentContact.editTextName.inputType = InputType.TYPE_CLASS_NUMBER
        binding.addAgentPincode.editTextName.inputType = InputType.TYPE_CLASS_NUMBER
        binding.addAgentContact.editTextName.filters = arrayOf(InputFilter.LengthFilter(10))
        binding.addAgentPincode.editTextName.filters = arrayOf(InputFilter.LengthFilter(6))

        binding.updateProfileBtn.setOnClickListener {
            updateProfile()
        }

    }

    private fun updateProfile() {
        val token="Bearer ${sharedPrefManager.getToken()}"
        val firstName=binding.addAgentFirstName.textInputLayout.editText!!.text.toString().trim()
        val lastName=binding.addAgentLastName.textInputLayout.editText!!.text.toString().trim()
        val contactNo=binding.addAgentContact.textInputLayout.editText!!.text.toString().trim()
        val address=binding.addAgentAddress.textInputLayout.editText!!.text.toString().trim()
        val state=binding.addAgentState.textInputLayout.editText!!.text.toString().trim()
        val city=binding.addAgentCity.textInputLayout.editText!!.text.toString().trim()
        val pinCode=binding.addAgentPincode.textInputLayout.editText!!.text.toString().trim()
        val country=binding.addAgentCountry.textInputLayout.editText!!.text.toString().trim()

        ApiClient.apiInterface.updateAgentProfile(token,firstName,lastName,contactNo,address,state,city,pinCode,country).enqueue(object : Callback<CommonResponse>{
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                if(response.isSuccessful){
                    Toast.makeText(this@EditProfileActivity,response.body()!!.message,Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(this@EditProfileActivity,response.body()!!.message,Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Toast.makeText(this@EditProfileActivity,t.message,Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun getProfile() {
        val token = "Bearer ${sharedPrefManager.getToken()}"

        ApiClient.apiInterface.getProfile(token).enqueue(object : Callback<ProfileResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                if (response.isSuccessful) {
                    response.body()?.profile?.let { data ->
                        binding.agentId.textInputLayout.editText?.setText(data.agentId)
                        binding.addAgentFirstName.textInputLayout.editText?.setText(data.firstName)
                        binding.addAgentLastName.textInputLayout.editText?.setText(data.lastName)
                        binding.addAgentEmail.textInputLayout.editText?.setText(data.agentEmail)
                        binding.addAgentContact.textInputLayout.editText?.setText(data.contactNo)
                        binding.addAgentPincode.textInputLayout.editText?.setText(data.pinCode)
                        binding.addAgentState.textInputLayout.editText?.setText(data.state)
                        binding.addAgentCity.textInputLayout.editText?.setText(data.city)
                        binding.addAgentCountry.textInputLayout.editText?.setText(data.country)
                        binding.addAgentAddress.textInputLayout.editText?.setText(data.address1)
                    } ?: run {
                        Toast.makeText(this@EditProfileActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@EditProfileActivity, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Toast.makeText(this@EditProfileActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}