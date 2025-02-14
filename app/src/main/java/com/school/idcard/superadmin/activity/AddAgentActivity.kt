package com.school.idcard.superadmin.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.school.idcard.R
import com.school.idcard.databinding.ActivityAddAgentBinding
import com.school.idcard.network.CommonResponse
import com.school.idcard.network.SharedPrefManager
import com.school.idcard.othermodel.AgentResponse
import com.school.idcard.superadmin.SuperAdminDashboard
import com.school.idcard.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddAgentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddAgentBinding
    private lateinit var sharedPrefManager: SharedPrefManager
    private var selectAgentType: String = "NORMAL"
    private var statusAgent: String = "Active"  // ✅ Default value
    private lateinit var agentId: String

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_agent)
        sharedPrefManager = SharedPrefManager(this)

        val type = intent.getIntExtra("type", 0)

        if (type == 1) {
            agentId = intent.getStringExtra("id") ?: ""
            if (agentId.isNotEmpty()) getAgentById(agentId)
            binding.toolbar.fileName.text = "Edit Agent"
            binding.pageType.text = "Edit Agent"
            binding.addAgentBtn.text = "Update Agent"
            binding.agentId.textInputLayout.visibility = View.VISIBLE
            binding.addAgentEmail.textInputLayout.isEnabled = false
        } else {
            binding.toolbar.fileName.text = "Add Agent"
            binding.pageType.text = "Add Agent"
            binding.addAgentBtn.text = "Create Agent"
            binding.agentId.textInputLayout.visibility = View.GONE
            binding.addAgentEmail.textInputLayout.isEnabled = true

        }

        binding.toolbar.backArrowBtn.setOnClickListener { finish() }

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

        // Setting DropDown for Agent Type
        val agentTypeArray = resources.getStringArray(R.array.agent_type_list)
        binding.autoCompleteTextView.setAdapter(ArrayAdapter(this, R.layout.dropdown_item_file, agentTypeArray))

        // Setting DropDown for Status
        val statusArray = resources.getStringArray(R.array.status_list)
        binding.autoCompleteTextView2.setAdapter(ArrayAdapter(this, R.layout.dropdown_item_file, statusArray))

        // Handling Agent Type Selection
        binding.autoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
            selectAgentType = if (parent.getItemAtPosition(position).toString() == "Regular Agent") {
                binding.editTextDate.visibility = View.GONE
                binding.editTextDate2.visibility = View.GONE
                "NORMAL"
            } else {
                binding.editTextDate.visibility = View.VISIBLE
                binding.editTextDate2.visibility = View.VISIBLE
                "PREMIUM"
            }
        }

        // Handling Status Selection
        binding.autoCompleteTextView2.setOnItemClickListener { parent, _, position, _ ->
            statusAgent = parent.getItemAtPosition(position).toString()
        }

        // Setting Date Pickers
        binding.editTextDate.isFocusable = false
        binding.editTextDate.setOnClickListener { showDatePickerDialog(binding.editTextDate) }

        binding.editTextDate2.isFocusable = false
        binding.editTextDate2.setOnClickListener { showDatePickerDialog(binding.editTextDate2) }

        // Handling Button Click
        binding.addAgentBtn.setOnClickListener {
            if (binding.addAgentBtn.text == "Update Agent") updateAgent() else addAgent()
        }
    }

    private fun updateAgent() {
        val token = "Bearer ${sharedPrefManager.getToken()}"
        val statusType = if (statusAgent == "Active") "true" else "false"

        ApiClient.apiInterface.updateAgent(
            token, agentId,
            binding.addAgentFirstName.editTextName.text!!.trim().toString(),
            binding.addAgentLastName.editTextName.text!!.trim().toString(),
            binding.addAgentEmail.editTextName.text!!.trim().toString(),
            binding.addAgentContact.editTextName.text!!.trim().toString(),
            selectAgentType,
            binding.addAgentAddress.editTextName.text!!.trim().toString(),
            "",
            binding.addAgentCity.editTextName.text!!.trim().toString(),
            binding.addAgentPincode.editTextName.text!!.trim().toString(),
            binding.addAgentState.editTextName.text!!.trim().toString(),
            binding.addAgentCountry.editTextName.text!!.trim().toString(),
            binding.editTextDate.text!!.trim().toString(),
            binding.editTextDate2.text!!.trim().toString(),
            statusType
        ).enqueue(handleResponse("Updated"))
    }

    private fun addAgent() {
        val token = "Bearer ${sharedPrefManager.getToken()}"
        val statusType = if (statusAgent == "Active") "true" else "false"

        ApiClient.apiInterface.addAgent(
            token,
            binding.addAgentFirstName.editTextName.text!!.trim().toString(),
            binding.addAgentLastName.editTextName.text!!.trim().toString(),
            binding.addAgentEmail.editTextName.text!!.trim().toString(),
            binding.addAgentContact.editTextName.text!!.trim().toString(),
            selectAgentType,
            binding.addAgentAddress.editTextName.text!!.trim().toString(),
            "",
            binding.addAgentCity.editTextName.text!!.trim().toString(),
            binding.addAgentPincode.editTextName.text!!.trim().toString(),
            binding.addAgentState.editTextName.text!!.trim().toString(),
            binding.addAgentCountry.editTextName.text!!.trim().toString(),
            binding.editTextDate.text!!.trim().toString(),
            binding.editTextDate2.text!!.trim().toString(),
            statusType
        ).enqueue(handleResponse("Created"))
    }

    private fun getAgentById(agentId: String) {
        val token = "Bearer ${sharedPrefManager.getToken()}"

        ApiClient.apiInterface.getAgentById(token, agentId).enqueue(object : Callback<AgentResponse> {
            override fun onResponse(call: Call<AgentResponse>, response: Response<AgentResponse>) {
                response.body()?.let { agentResponse ->
                    val agent = agentResponse.agent

                    binding.agentId.editTextName.setText(agent.agentId)
                    binding.addAgentFirstName.editTextName.setText(agent.firstName)
                    binding.addAgentLastName.editTextName.setText(agent.lastName)
                    binding.addAgentEmail.editTextName.setText(agent.agentEmail)
                    binding.addAgentContact.editTextName.setText(agent.contactNo)
                    binding.addAgentAddress.editTextName.setText(agent.address1)
                    binding.addAgentCity.editTextName.setText(agent.city)
                    binding.addAgentPincode.editTextName.setText(agent.pinCode)
                    binding.addAgentState.editTextName.setText(agent.state)
                    binding.addAgentCountry.editTextName.setText(agent.country)

                    // Set status
                    statusAgent = if (agent.status) "Active" else "In-Active"
                    binding.autoCompleteTextView2.setText(statusAgent, false)

                    // Set agent type
                    selectAgentType = if (agent.agentType == "NORMAL") "NORMAL" else "PREMIUM"
                    binding.autoCompleteTextView.setText(selectAgentType, false)

                    // Set subscription dates
                    binding.editTextDate.setText(agent.startSub ?: "")
                    binding.editTextDate2.setText(agent.endSub ?: "")
                }
            }

            override fun onFailure(call: Call<AgentResponse>, t: Throwable) {
                Toast.makeText(this@AddAgentActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(this, { _, year, month, day ->
            val selectedDate = Calendar.getInstance().apply { set(year, month, day) }
            editText.setText(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.time))
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

        datePickerDialog.show()
    }

    private fun handleResponse(action: String) = object : Callback<CommonResponse> {
        override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
            Toast.makeText(this@AddAgentActivity, response.body()?.message ?: "$action Failed", Toast.LENGTH_SHORT).show()
            if (response.isSuccessful) {
                startActivity(Intent(this@AddAgentActivity, SuperAdminDashboard::class.java))
                finish()
            }
        }

        override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
            Toast.makeText(this@AddAgentActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
