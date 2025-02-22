package com.school.idcard.superadmin.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.school.idcard.R
import com.school.idcard.databinding.ActivityEditSchoolBinding
import com.school.idcard.network.ApiClient
import com.school.idcard.network.SharedPrefManager
import com.school.idcard.othermodel.AgentResponse2
import com.school.idcard.othermodel.GetSchoolResponse
import com.school.idcard.othermodel.PrintDetail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditSchoolActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditSchoolBinding
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var printDetailsAdapter: PrintDetailsAdapter
    private var role: String = "SUPERADMIN"
    private var selectedAgentId: String? = ""
    private val agentMap = mutableMapOf<String, String>() // Key: Name, Value: ID
    val selectedFields = mutableListOf<String>()
    private val printDetails = mutableListOf<PrintDetail>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_school)
        sharedPrefManager = SharedPrefManager(this)
        role = sharedPrefManager.getRole().toString()

        // Toolbar actions
        binding.toolbar.backArrowBtn.setOnClickListener { finish() }

        // Get school ID from intent
        val schoolId = intent.getIntExtra("schoolId", 0).toString()

        // Fetch school details
        getSchoolById(schoolId)

        // UI setup
        binding.toolbar.fileName.text = "Edit School"
        binding.addSchoolBtn.text = "Update School"
        binding.schoolIdEt.textInputLayout.visibility = View.VISIBLE
        binding.schoolEmailEt.textInputLayout.isEnabled = false

        if (role == "SUPERADMIN") {
            fetchAgents()
            binding.agentSpinner.visibility = View.VISIBLE
        } else {
            binding.agentSpinner.visibility = View.GONE
        }

        binding.autoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
            val selectedAgentName = parent.getItemAtPosition(position).toString()
            selectedAgentId = agentMap[selectedAgentName]
        }


        // Set input hints
        setupInputHints()

        // Set up status dropdown
        setupStatusDropdown()

        // Expand/collapse sections
        setupSectionToggle()

        // RecyclerView for Print Details
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        printDetailsAdapter = PrintDetailsAdapter(printDetails) { updatedList ->
            selectedFields.clear()
            selectedFields.addAll(updatedList.map { it.keyName })  // Extract only keyName
        }

// Fetch school details
        getSchoolById(schoolId)


        // Handle update button click
        binding.addSchoolBtn.setOnClickListener { updateSchool() }
    }

    private fun setupInputHints() {
        binding.schoolIdEt.textInputLayout.hint = "School Id"
        binding.schoolNameEt.textInputLayout.hint = "School Name"
        binding.schoolContactEt.textInputLayout.hint = "Contact Number"
        binding.schoolEmailEt.textInputLayout.hint = "Email Id"
        binding.schoolAddressEt.textInputLayout.hint = "Address Details"
        binding.schoolCityEt.textInputLayout.hint = "City"
        binding.schoolStateEt.textInputLayout.hint = "State"
        binding.schoolPincodeEt.textInputLayout.hint = "Pincode"
        binding.schoolCountryEt.textInputLayout.hint = "Country"
        binding.schoolPrincipalEt.textInputLayout.hint = "Principal Name"

        binding.schoolContactEt.editTextName.inputType = InputType.TYPE_CLASS_NUMBER
        binding.schoolPincodeEt.editTextName.inputType = InputType.TYPE_CLASS_NUMBER
        binding.schoolContactEt.editTextName.filters = arrayOf(InputFilter.LengthFilter(10))
        binding.schoolPincodeEt.editTextName.filters = arrayOf(InputFilter.LengthFilter(6))
    }

    private fun setupStatusDropdown() {
        val statusList = resources.getStringArray(R.array.status_list)
        val statusAdapter = ArrayAdapter(this, R.layout.dropdown_item_file, statusList)
        binding.autoCompleteTextView2.setAdapter(statusAdapter)
    }

    private fun setupSectionToggle() {
        binding.linearLayout8.setOnClickListener {
            toggleSection(binding.schoolDetailsForm, binding.arrowImage)
        }
        binding.linearLayout9.setOnClickListener {
            toggleSection(binding.idCardDetailsForm, binding.arrowImage)
        }
    }

    private fun toggleSection(section: View, arrow: View) {
        if (section.visibility == View.VISIBLE) {
            section.visibility = View.GONE
            arrow.setBackgroundResource(R.drawable.arrow_down)
        } else {
            section.visibility = View.VISIBLE
            arrow.setBackgroundResource(R.drawable.arrow_up)
        }
    }

    private fun getSchoolById(schoolId: String) {
        val token = "Bearer ${sharedPrefManager.getToken()}"

        ApiClient.apiInterface.getSchoolViaId(token, schoolId).enqueue(object :
            Callback<GetSchoolResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<GetSchoolResponse>, response: Response<GetSchoolResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val schoolResponse = response.body()

                    // Ensure the schoolDetails object is not null
                    val schoolDetails = schoolResponse?.schoolDetails
                    if (schoolDetails != null) {
                        binding.schoolIdEt.editTextName.setText(schoolDetails.schoolId)
                        binding.schoolNameEt.editTextName.setText(schoolDetails.schoolName)
                        binding.schoolEmailEt.editTextName.setText(schoolDetails.schoolEmail)
                        binding.schoolContactEt.editTextName.setText(schoolDetails.contactNo)
                        binding.schoolAddressEt.editTextName.setText(schoolDetails.address1)
                        binding.schoolCityEt.editTextName.setText(schoolDetails.city)
                        binding.schoolPincodeEt.editTextName.setText(schoolDetails.pinCode)
                        binding.schoolStateEt.editTextName.setText(schoolDetails.state)
                        binding.schoolCountryEt.editTextName.setText(schoolDetails.country)
                        binding.schoolPrincipalEt.editTextName.setText(schoolDetails.principalName)

                        // Set agent name in dropdown if available
                        if (!schoolDetails.agentName.isNullOrEmpty()) {
                            binding.autoCompleteTextView.setText(schoolDetails.agentName, false)
                            selectedAgentId = schoolDetails.agentId
                        }

                        // Set status
                        binding.autoCompleteTextView2.setText(schoolDetails.status, false)

                        // Update print details checkbox
                        schoolResponse.printDetails.let { updatePrintDetails(it) }

                    } else {
                        Toast.makeText(this@EditSchoolActivity, "School details not found!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@EditSchoolActivity, "Failed to load school details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GetSchoolResponse>, t: Throwable) {
                Toast.makeText(this@EditSchoolActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updatePrintDetails(apiPrintDetails: List<PrintDetail>) {
        printDetails.clear()
        printDetails.addAll(apiPrintDetails)
        binding.recyclerView.adapter?.notifyDataSetChanged()
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
                        val arrayAdapter = ArrayAdapter(this@EditSchoolActivity, R.layout.dropdown_item_file, agentNames)
                        binding.autoCompleteTextView.setAdapter(arrayAdapter)
                    } else {
                        Toast.makeText(this@EditSchoolActivity, "No agents found.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<AgentResponse2>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }


    private fun updateSchool() {
//        val token = "Bearer ${sharedPrefManager.getToken()}"
//
//        val request = UpdateSchoolRequest(
//            schoolId = binding.schoolIdEt.editTextName.text.toString().trim(),
//            schoolName = binding.schoolNameEt.editTextName.text.toString().trim(),
//            contactNo = binding.schoolContactEt.editTextName.text.toString().trim(),
//            address1 = binding.schoolAddressEt.editTextName.text.toString().trim(),
//            city = binding.schoolCityEt.editTextName.text.toString().trim(),
//            pinCode = binding.schoolPincodeEt.editTextName.text.toString().trim(),
//            state = binding.schoolStateEt.editTextName.text.toString().trim(),
//            country = binding.schoolCountryEt.editTextName.text.toString().trim(),
//            schoolEmail = binding.schoolEmailEt.editTextName.text.toString().trim(),
//            principalName = binding.schoolPrincipalEt.editTextName.text.toString().trim(),
//            agentId = selectedAgentId,
//            status = binding.autoCompleteTextView2.text.toString().trim()
//        )
//
//        ApiClient.apiInterface.updateSchool(token, request).enqueue(handleResponse("Update"))
    }
}
