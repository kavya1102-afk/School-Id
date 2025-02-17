package com.school.idcard.superadmin.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.school.idcard.R
import com.school.idcard.databinding.ActivityAddSchoolBinding
import com.school.idcard.network.ApiClient
import com.school.idcard.network.CommonResponse
import com.school.idcard.network.SharedPrefManager
import com.school.idcard.othermodel.AgentResponse2
import com.school.idcard.superadmin.SuperAdminDashboard
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddSchoolActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAddSchoolBinding
    private lateinit var sharedPrefManager: SharedPrefManager
    private var status2:String="Active"
    private var role:String="SUPERADMIN"
    private lateinit var selectedAgentId:String
    val selectedFields = mutableListOf<String>()
    private val agentMap = mutableMapOf<String, String>() // Key: Name, Value: ID

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_add_school)
        sharedPrefManager=SharedPrefManager(this)
        role=sharedPrefManager.getRole().toString()

        binding.toolbar.fileName.text="Add School"
        binding.toolbar.backArrowBtn.setOnClickListener { finish() }

        val Id=intent.getIntExtra("type",0)
        if(Id==1){
            binding.schoolIdEt.textInputLayout.visibility=View.VISIBLE
        }else {
            binding.schoolIdEt.textInputLayout.visibility=View.GONE
        }

        if(role=="SUPERADMIN"){
            fetchAgents()
            binding.agentSpinner.visibility=View.VISIBLE
        }else{
            binding.agentSpinner.visibility=View.GONE
        }

        binding.autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedAgentName = parent.getItemAtPosition(position).toString()
            // Retrieve the agent ID using the selected name
            selectedAgentId = agentMap[selectedAgentName].toString()

            // Now you can use the selectedAgentId wherever necessary, for example:
            Toast.makeText(this@AddSchoolActivity, "Selected Agent ID: $selectedAgentId", Toast.LENGTH_SHORT).show()
        }


        binding.schoolIdEt.textInputLayout.hint="School Id"
        binding.schoolNameEt.textInputLayout.hint="School Name"
        binding.schoolContactEt.textInputLayout.hint="Contact Number"
        binding.schoolEmailEt.textInputLayout.hint="Email Id"
        binding.schoolAddressEt.textInputLayout.hint="Address Details"
        binding.schoolCityEt.textInputLayout.hint="City"
        binding.schoolStateEt.textInputLayout.hint="State"
        binding.schoolPincodeEt.textInputLayout.hint="Pincode"
        binding.schoolCountryEt.textInputLayout.hint="Country"
        binding.schoolPrincipalEt.textInputLayout.hint="Principal Name"

        binding.schoolContactEt.editTextName.inputType = InputType.TYPE_CLASS_NUMBER
        binding.schoolPincodeEt.editTextName.inputType = InputType.TYPE_CLASS_NUMBER
        val maxLength = 10
        val maxLength2 = 6
        binding.schoolContactEt.editTextName.filters = arrayOf(InputFilter.LengthFilter(maxLength))
        binding.schoolPincodeEt.editTextName.filters = arrayOf(InputFilter.LengthFilter(maxLength2))


        val status = resources.getStringArray(R.array.status_list)
        val arrayAdapter2 = ArrayAdapter(this, R.layout.dropdown_item_file, status)
        binding.autoCompleteTextView2.setAdapter(arrayAdapter2)

        binding.linearLayout8.setOnClickListener {
            if (binding.schoolDetailsForm.visibility == View.VISIBLE) {
                binding.schoolDetailsForm.visibility = View.GONE
                binding.arrowImage.setImageResource(R.drawable.arrow_down) // Change to down arrow
            } else {
                binding.schoolDetailsForm.visibility = View.VISIBLE
                binding.arrowImage.setImageResource(R.drawable.arrow_up) // Change to down arrow
            }
        }

        binding.linearLayout9.setOnClickListener {
            if (binding.idCardDetailsForm.visibility == View.VISIBLE) {
                binding.idCardDetailsForm.visibility = View.GONE
                binding.arrowImage.setImageResource(R.drawable.arrow_down) // Change to down arrow
            } else {
                binding.idCardDetailsForm.visibility = View.VISIBLE
                binding.arrowImage.setImageResource(R.drawable.arrow_up) // Change to down arrow
            }
        }

        val checkBoxList = listOf(
            binding.checkBoxStudentID to "Student ID",
            binding.checkBoxClass to "Class",
            binding.checkBoxStudentName to "Student Name",
            binding.checkBoxSection to "Section",
            binding.checkBoxContactNumber to "Contact Number",
            binding.checkBoxGender to "Gender",
            binding.checkBoxDOB to "Date of Birth",
            binding.checkBoxMotherName to "Mother Name",
            binding.checkBoxFatherName to "Father Name",
            binding.checkBoxAddress to "Address",
            binding.checkBoxUploadPhoto to "Upload Photo"
        )

// Listen to checkbox changes and update the selectedFields list
        checkBoxList.forEach { (checkBox, fieldName) ->
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    if (!selectedFields.contains(fieldName)) {
                        selectedFields.add(fieldName)
                    }
                } else {
                    selectedFields.remove(fieldName)
                }
            }
        }

        binding.addSchoolBtn.setOnClickListener {
//            Log.d("selectedFields", "onCreate: $selectedFields")
            if(validateFields()){
                addSchool()
            }else{
                Toast.makeText(this,"Something is wrong",Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun addSchool() {
        val schoolName = binding.schoolNameEt.textInputLayout.editText?.text.toString().trim()
        val contactNumber = binding.schoolContactEt.textInputLayout.editText?.text.toString().trim()
        val email = binding.schoolEmailEt.textInputLayout.editText?.text.toString().trim()
        val address = binding.schoolAddressEt.textInputLayout.editText?.text.toString().trim()
        val city = binding.schoolCityEt.textInputLayout.editText?.text.toString().trim()
        val state = binding.schoolStateEt.textInputLayout.editText?.text.toString().trim()
        val pincode = binding.schoolPincodeEt.textInputLayout.editText?.text.toString().trim()
        val country = binding.schoolCountryEt.textInputLayout.editText?.text.toString().trim()
        val principalName = binding.schoolPrincipalEt.textInputLayout.editText?.text.toString().trim()
        val token="Bearer ${sharedPrefManager.getToken()}"

        ApiClient.apiInterface.addSchool(token,schoolName,contactNumber,address,"",city,pincode,state,country,email,principalName,selectedAgentId,status2,
            selectedFields).enqueue(handleResponse("Add"))

    }

    private fun validateFields(): Boolean {
//        val schoolId = binding.schoolIdEt.textInputLayout.editText?.text.toString().trim()
        val schoolName = binding.schoolNameEt.textInputLayout.editText?.text.toString().trim()
        val contactNumber = binding.schoolContactEt.textInputLayout.editText?.text.toString().trim()
        val email = binding.schoolEmailEt.textInputLayout.editText?.text.toString().trim()
        val address = binding.schoolAddressEt.textInputLayout.editText?.text.toString().trim()
        val city = binding.schoolCityEt.textInputLayout.editText?.text.toString().trim()
        val state = binding.schoolStateEt.textInputLayout.editText?.text.toString().trim()
        val pincode = binding.schoolPincodeEt.textInputLayout.editText?.text.toString().trim()
        val country = binding.schoolCountryEt.textInputLayout.editText?.text.toString().trim()
        val principalName = binding.schoolPrincipalEt.textInputLayout.editText?.text.toString().trim()


        if (schoolName.isEmpty()) {
            binding.schoolNameEt.textInputLayout.error = "School Name is required"
            return false
        }
        if (contactNumber.isEmpty()) {
            binding.schoolContactEt.textInputLayout.error = "Contact Number is required"
            return false
        }
        if (contactNumber.length != 10) { // Assuming contact number should be exactly 10 digits
            binding.schoolContactEt.textInputLayout.error = "Contact Number must be 10 digits"
            return false
        }
        if (email.isEmpty()) {
            binding.schoolEmailEt.textInputLayout.error = "Email is required"
            return false
        }
        if (address.isEmpty()) {
            binding.schoolAddressEt.textInputLayout.error = "Address is required"
            return false
        }
        if (city.isEmpty()) {
            binding.schoolCityEt.textInputLayout.error = "City is required"
            return false
        }
        if (state.isEmpty()) {
            binding.schoolStateEt.textInputLayout.error = "State is required"
            return false
        }
        if (pincode.isEmpty()) {
            binding.schoolPincodeEt.textInputLayout.error = "Pincode is required"
            return false
        }
        if (country.isEmpty()) {
            binding.schoolCountryEt.textInputLayout.error = "Country is required"
            return false
        }
        if (principalName.isEmpty()) {
            binding.schoolPrincipalEt.textInputLayout.error = "Principal Name is required"
            return false
        }

        // Clear errors if all validations pass
        binding.schoolIdEt.textInputLayout.error = null
        binding.schoolNameEt.textInputLayout.error = null
        binding.schoolContactEt.textInputLayout.error = null
        binding.schoolEmailEt.textInputLayout.error = null
        binding.schoolAddressEt.textInputLayout.error = null
        binding.schoolCityEt.textInputLayout.error = null
        binding.schoolStateEt.textInputLayout.error = null
        binding.schoolPincodeEt.textInputLayout.error = null
        binding.schoolCountryEt.textInputLayout.error = null
        binding.schoolPrincipalEt.textInputLayout.error = null

        return true
    }

    private fun handleResponse(action: String) = object : Callback<CommonResponse> {
        override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
            Toast.makeText(this@AddSchoolActivity, response.body()?.message ?: "$action Failed", Toast.LENGTH_SHORT).show()
            if (response.isSuccessful) {
                startActivity(Intent(this@AddSchoolActivity, SuperAdminDashboard::class.java))
                finish()
            }
        }

        override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
            Toast.makeText(this@AddSchoolActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchAgents() {
        val token="Bearer ${sharedPrefManager.getToken()}"
        ApiClient.apiInterface.getAgentList(token).enqueue(object : Callback<AgentResponse2>{
            override fun onResponse(call: Call<AgentResponse2>, response: Response<AgentResponse2>) {
                if(response.isSuccessful){
                    val agentResponse = response.body()
                    if (agentResponse != null && agentResponse.agents.isNotEmpty()) {
                        val agentNames = agentResponse.agents.map { it.agentFirstName + " " + it.agentLastName }

                        // Populate the agentMap with Name as key and ID as value
                        agentResponse.agents.forEach {
                            agentMap[it.agentFirstName + " " + it.agentLastName] = it.agentId
                        }

                        // Set the adapter for AutoCompleteTextView
                        val arrayAdapter = ArrayAdapter(this@AddSchoolActivity, R.layout.dropdown_item_file, agentNames)
                        binding.autoCompleteTextView.setAdapter(arrayAdapter)
                    } else {
                        Toast.makeText(this@AddSchoolActivity, "No agents found.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<AgentResponse2>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }


}