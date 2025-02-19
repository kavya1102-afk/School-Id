package com.school.idcard.superadmin.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.school.idcard.R
import com.school.idcard.databinding.ActivityAddSchoolBinding
import com.school.idcard.network.ApiClient
import com.school.idcard.network.CommonResponse
import com.school.idcard.network.SharedPrefManager
import com.school.idcard.othermodel.AddSchoolRequest
import com.school.idcard.othermodel.AgentResponse2
import com.school.idcard.othermodel.GetSchoolResponse
import com.school.idcard.othermodel.PrintDetail
import com.school.idcard.superadmin.SuperAdminDashboard
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddSchoolActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddSchoolBinding
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var adapter: PrintDetailsAdapter
    private var status2: String = "Active"
    private var role: String = "SUPERADMIN"
    private lateinit var schoolId: String
    private var selectedAgentId: String? = ""
    val selectedFields = mutableListOf<String>()
    private val agentMap = mutableMapOf<String, String>() // Key: Name, Value: ID
    var printDetails = mutableListOf(
        PrintDetail("Student ID", false),
        PrintDetail("Class", false),
        PrintDetail("Student Name", false),
        PrintDetail("Section", false),
        PrintDetail("Contact Number", false),
        PrintDetail("Gender", false),
        PrintDetail("Date of Birth", false),
        PrintDetail("Mother Name", false),
        PrintDetail("Father Name", false),
        PrintDetail("Address", false),
        PrintDetail("Upload Photo", false)
    )

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_school)
        sharedPrefManager = SharedPrefManager(this)
        role = sharedPrefManager.getRole().toString()

        binding.toolbar.backArrowBtn.setOnClickListener { finish() }

        val type = intent.getIntExtra("type", 0)
        val schoolId = intent.getIntExtra("schoolId", 0)

        if (type == 1) {
            getSchoolById(schoolId.toString())
//            agentId = intent.getStringExtra("id") ?: ""
            binding.toolbar.fileName.text = "Edit School"
            binding.addSchoolBtn.text = "Update School"
            binding.schoolIdEt.textInputLayout.visibility = View.VISIBLE
            binding.schoolEmailEt.textInputLayout.isEnabled = false
        } else {
            binding.toolbar.fileName.text = "Add School"
            binding.addSchoolBtn.text = "Create School"
            binding.schoolIdEt.textInputLayout.visibility = View.GONE
            binding.schoolEmailEt.textInputLayout.isEnabled = true

        }

        binding.customizeDetailsBtn.setOnClickListener {
            binding.addFieldLayout.visibility = View.VISIBLE
        }

        if (role == "SUPERADMIN") {
            fetchAgents()
            binding.agentSpinner.visibility = View.VISIBLE
        } else {
            binding.agentSpinner.visibility = View.GONE
        }

        binding.autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedAgentName = parent.getItemAtPosition(position).toString()
            // Retrieve the agent ID using the selected name
            selectedAgentId = agentMap[selectedAgentName].toString()

            // Now you can use the selectedAgentId wherever necessary, for example:
            Toast.makeText(
                this@AddSchoolActivity,
                "Selected Agent ID: $selectedAgentId",
                Toast.LENGTH_SHORT
            ).show()
        }


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
        binding.addField.textInputLayout.hint = "Add Custom Field"

        binding.schoolContactEt.editTextName.inputType = InputType.TYPE_CLASS_NUMBER
        binding.schoolPincodeEt.editTextName.inputType = InputType.TYPE_CLASS_NUMBER
        val maxLength = 10
        val maxLength2 = 6
        binding.schoolContactEt.editTextName.filters = arrayOf(InputFilter.LengthFilter(maxLength))
        binding.schoolPincodeEt.editTextName.filters = arrayOf(InputFilter.LengthFilter(maxLength2))


        val status = resources.getStringArray(R.array.status_list)
        val arrayAdapter2 = ArrayAdapter(this, R.layout.dropdown_item_file, status)
        binding.autoCompleteTextView2.setAdapter(arrayAdapter2)

        // Handling Agent Type Selection
        binding.autoCompleteTextView2.setOnItemClickListener { parent, _, position, _ ->
            status2 = parent.getItemAtPosition(position).toString()
        }

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

        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = PrintDetailsAdapter(printDetails) { updatedList ->
            selectedFields.clear()
            selectedFields.addAll(updatedList.map { it.keyName })
        }
        binding.recyclerView.adapter = adapter



// Handle custom field addition
        binding.addCustomFieldBtn.setOnClickListener {
            val customFieldName = binding.addField.textInputLayout.editText?.text.toString().trim()

            if (customFieldName.isNotEmpty()) {
                adapter.addCustomField(customFieldName) // Add the custom field
                binding.addField.textInputLayout.editText?.text?.clear() // Clear input
            } else {
                Toast.makeText(this, "Enter a field name!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.addSchoolBtn.setOnClickListener {
            if (type == 1) {
                updateSchool(schoolId.toString())
            } else {
                if (validateFields()) {
                    addSchool()
                }
            }
        }

    }

    private fun updateSchool(schoolId: String) {
        val selectedPrintDetails = printDetails.map {
            PrintDetail(it.keyName, it.isChecked)
        }

        val request = AddSchoolRequest(
            schoolName = binding.schoolNameEt.textInputLayout.editText?.text.toString().trim(),
            contactNo = binding.schoolContactEt.textInputLayout.editText?.text.toString().trim(),
            address1 = binding.schoolAddressEt.textInputLayout.editText?.text.toString().trim(),
            address2 = "", // Address2 is empty if not used
            city = binding.schoolCityEt.textInputLayout.editText?.text.toString().trim(),
            pinCode = binding.schoolPincodeEt.textInputLayout.editText?.text.toString().trim(),
            state = binding.schoolStateEt.textInputLayout.editText?.text.toString().trim(),
            country = binding.schoolCountryEt.textInputLayout.editText?.text.toString().trim(),
            schoolEmail = binding.schoolEmailEt.textInputLayout.editText?.text.toString().trim(),
            principalName = binding.schoolPrincipalEt.textInputLayout.editText?.text.toString()
                .trim(),
            agentId = selectedAgentId,
            status = status2,
            addField = selectedPrintDetails // Pass the list of PrintDetailRequest
        )

        val token = "Bearer ${sharedPrefManager.getToken()}"

        ApiClient.apiInterface.updateSchool(token, request, schoolId).enqueue(handleResponse("Update"))    }


    private fun addSchool() {
        val selectedPrintDetails = printDetails.map {
            PrintDetail(it.keyName, it.isChecked)
        }

        val request = AddSchoolRequest(
            schoolName = binding.schoolNameEt.textInputLayout.editText?.text.toString().trim(),
            contactNo = binding.schoolContactEt.textInputLayout.editText?.text.toString().trim(),
            address1 = binding.schoolAddressEt.textInputLayout.editText?.text.toString().trim(),
            address2 = "", // Address2 is empty if not used
            city = binding.schoolCityEt.textInputLayout.editText?.text.toString().trim(),
            pinCode = binding.schoolPincodeEt.textInputLayout.editText?.text.toString().trim(),
            state = binding.schoolStateEt.textInputLayout.editText?.text.toString().trim(),
            country = binding.schoolCountryEt.textInputLayout.editText?.text.toString().trim(),
            schoolEmail = binding.schoolEmailEt.textInputLayout.editText?.text.toString().trim(),
            principalName = binding.schoolPrincipalEt.textInputLayout.editText?.text.toString()
                .trim(),
            agentId = selectedAgentId,
            status = status2,
            addField = selectedPrintDetails // Pass the list of PrintDetailRequest
        )

        val token = "Bearer ${sharedPrefManager.getToken()}"

        ApiClient.apiInterface.addSchool(token, request).enqueue(handleResponse("Add"))

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
        val principalName =
            binding.schoolPrincipalEt.textInputLayout.editText?.text.toString().trim()


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
            Toast.makeText(
                this@AddSchoolActivity,
                response.body()?.message ?: "$action Failed",
                Toast.LENGTH_SHORT
            ).show()
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
        val token = "Bearer ${sharedPrefManager.getToken()}"
        ApiClient.apiInterface.getAgentList(token).enqueue(object : Callback<AgentResponse2> {
            override fun onResponse(
                call: Call<AgentResponse2>,
                response: Response<AgentResponse2>
            ) {
                if (response.isSuccessful) {
                    val agentResponse = response.body()
                    if (agentResponse != null && agentResponse.agents.isNotEmpty()) {
                        val agentNames =
                            agentResponse.agents.map { it.agentFirstName + " " + it.agentLastName }

                        // Populate the agentMap with Name as key and ID as value
                        agentResponse.agents.forEach {
                            agentMap[it.agentFirstName + " " + it.agentLastName] = it.agentId
                        }

                        // Set the adapter for AutoCompleteTextView
                        val arrayAdapter = ArrayAdapter(
                            this@AddSchoolActivity,
                            R.layout.dropdown_item_file,
                            agentNames
                        )
                        binding.autoCompleteTextView.setAdapter(arrayAdapter)
                    } else {
                        Toast.makeText(
                            this@AddSchoolActivity,
                            "No agents found.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<AgentResponse2>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun getSchoolById(schoolId: String) {
        val token = "Bearer ${sharedPrefManager.getToken()}"

        ApiClient.apiInterface.getSchoolViaId(token, schoolId)
            .enqueue(object : Callback<GetSchoolResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<GetSchoolResponse>,
                    response: Response<GetSchoolResponse>
                ) {
                    response.body()?.let { schoolResponse ->
                        val agent = schoolResponse.schoolDetails

                        binding.schoolIdEt.editTextName.setText(agent.agentId)
                        binding.schoolNameEt.editTextName.setText(agent.schoolName)
                        binding.schoolEmailEt.editTextName.setText(agent.schoolEmail)
                        binding.schoolContactEt.editTextName.setText(agent.contactNo)
                        binding.schoolAddressEt.editTextName.setText(agent.address1)
                        binding.schoolCityEt.editTextName.setText(agent.city)
                        binding.schoolPincodeEt.editTextName.setText(agent.pinCode)
                        binding.schoolStateEt.editTextName.setText(agent.state)
                        binding.schoolCountryEt.editTextName.setText(agent.country)
                        binding.schoolPrincipalEt.editTextName.setText(agent.principalName)

                        // Clear existing data and update with the new list
                        // Clear existing data
                        printDetails.clear()
// Add new data
                        printDetails = schoolResponse.printDetails.map {
                            PrintDetail(it.keyName, it.isChecked)
                        }.toMutableList()

                        // 🔹 Update Adapter
                        adapter.updateData(printDetails)

                        Log.d("RecyclerViewDebug", "Fetched school details and updating RecyclerView")
                        Log.d("RecyclerViewDebug", "Updated list size: ${printDetails.size}")
                        printDetails.forEach {
                            Log.d("RecyclerViewDebug", "Item: ${it.keyName}, isChecked: ${it.isChecked}")
                        }


                        // Set status
                        binding.autoCompleteTextView2.setText(status2, false)
                        binding.autoCompleteTextView.setText(agent.agentName, false)


                    }
                }

                override fun onFailure(call: Call<GetSchoolResponse>, t: Throwable) {
                    Toast.makeText(
                        this@AddSchoolActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

}


class PrintDetailsAdapter(
    private var items: MutableList<PrintDetail>,
    private val onSelectionChanged: (List<PrintDetail>) -> Unit
) : RecyclerView.Adapter<PrintDetailsAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.checkBox)
        val textView: TextView = view.findViewById(R.id.textView)

        fun bind(item: PrintDetail) {
            textView.text = item.keyName
            checkBox.isChecked = item.isChecked

            checkBox.setOnCheckedChangeListener(null) // Prevent unwanted callbacks
            checkBox.isChecked = item.isChecked

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                item.isChecked = isChecked
                onSelectionChanged(items)
            }

            Log.d("RecyclerViewDebug", "Binding item: ${item.keyName}, isChecked: ${item.isChecked}")
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_checkbox, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    // Method to update data
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<PrintDetail>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged() // Notify the adapter
    }

    // Method to add a new field dynamically
    fun addCustomField(fieldName: String) {
        if (!items.any { it.keyName == fieldName }) {
            val newField = PrintDetail(fieldName, true)
            items.add(newField)
            notifyItemInserted(items.size - 1)
            onSelectionChanged(items)
        }
    }
}


