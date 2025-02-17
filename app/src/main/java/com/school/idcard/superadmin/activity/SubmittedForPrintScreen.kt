package com.school.idcard.superadmin.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.school.idcard.R
import com.school.idcard.agent.SubmittedPrintForNormalAgentActivity
import com.school.idcard.databinding.ActivitySubmittedForPrintScreenBinding
import com.school.idcard.network.ApiClient
import com.school.idcard.network.SharedPrefManager
import com.school.idcard.othermodel.SchoolsResponse2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubmittedForPrintScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySubmittedForPrintScreenBinding
    private lateinit var sharedPrefManager: SharedPrefManager
    private val schoolMap = mutableMapOf<String, String>() // Key: Name, Value: ID
    private var selectedSchoolId: String? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_submitted_for_print_screen)
        sharedPrefManager = SharedPrefManager(this)

        binding.toolbar.fileName.text="Submitted For Print"
        binding.toolbar.backArrowBtn.setOnClickListener { finish() }

        fetchSchools()

        binding.autoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
            val selectedSchoolName = parent.getItemAtPosition(position).toString()
            selectedSchoolId = schoolMap[selectedSchoolName] // Get the actual ID

            // Show a proper message
            if (selectedSchoolId != null) {
                Toast.makeText(this, "Selected School ID: $selectedSchoolId", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "School not found.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.searchBtn.setOnClickListener {
            val role=sharedPrefManager.getRole().toString()
            if(role=="PREMIUM"){
                startActivity(Intent(this,SchoolDetailsActivitySuperAdmin::class.java)
                    .putExtra("schoolId",selectedSchoolId))
            }else{
                startActivity(Intent(this,SubmittedPrintForNormalAgentActivity::class.java)
                    .putExtra("schoolId",selectedSchoolId))
            }
        }

    }

    private fun fetchSchools() {
        val token = "Bearer ${sharedPrefManager.getToken()}"
        ApiClient.apiInterface.getSchoolList(token).enqueue(object : Callback<SchoolsResponse2> {
            override fun onResponse(call: Call<SchoolsResponse2>, response: Response<SchoolsResponse2>) {
                if (response.isSuccessful) {
                    val schoolResponse = response.body()
                    if (!schoolResponse?.schools.isNullOrEmpty()) {
                        val schoolNames = schoolResponse!!.schools!!.map { it.schoolName ?: "Unknown" }

                        // Populate the schoolMap with Name as key and ID as value
                        schoolResponse.schools!!.forEach {
                            if (it.schoolName != null && it.schoolId != null) {
                                schoolMap[it.schoolName] = it.schoolId
                            }
                        }

                        // Set the adapter for AutoCompleteTextView
                        val arrayAdapter = ArrayAdapter(
                            this@SubmittedForPrintScreen,
                            R.layout.dropdown_item_file,
                            schoolNames
                        )
                        binding.autoCompleteTextView.setAdapter(arrayAdapter)
                    } else {
                        Toast.makeText(this@SubmittedForPrintScreen, "No schools found.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@SubmittedForPrintScreen, "Failed to fetch schools.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SchoolsResponse2>, t: Throwable) {
                Toast.makeText(this@SubmittedForPrintScreen, "Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
