package com.school.idcard.admin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputFilter
import android.text.InputType
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.school.idcard.R
import com.school.idcard.databinding.ActivityAddStaffBinding
import com.school.idcard.network.ApiClient
import com.school.idcard.network.SharedPrefManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

@Suppress("DEPRECATION")
class AddStaffActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStaffBinding
    private lateinit var sharedPrefManager: SharedPrefManager
    private var status2: String = "Active"
    private var roleType: String = "0"
    private val REQUEST_CODE_IMAGE = 111
    private var selectedImageUri: Uri? = null
    private var selectedImageFile: File? = null // Store image file
    private val roleMap = HashMap<String, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_staff)
        sharedPrefManager = SharedPrefManager(this)

        fetchRolesFromApi()

        binding.staffName.textInputLayout.hint="Staff Name"
        binding.staffContact.textInputLayout.hint="Staff Contact"
        binding.staffAddress.textInputLayout.hint="Staff Address"
        binding.staffCountry.textInputLayout.hint="Staff Country"
        binding.staffPincode.textInputLayout.hint="Staff PinCode"
        binding.staffCity.textInputLayout.hint="Staff City"
        binding.staffEmail.textInputLayout.hint="Staff Email"
        binding.staffState.textInputLayout.hint="Staff State"

        binding.staffContact.editTextName.inputType = InputType.TYPE_CLASS_NUMBER
        binding.staffPincode.editTextName.inputType = InputType.TYPE_CLASS_NUMBER
        val maxLength = 10
        val maxLength2 = 6
        binding.staffContact.editTextName.filters = arrayOf(InputFilter.LengthFilter(maxLength))
        binding.staffPincode.editTextName.filters = arrayOf(InputFilter.LengthFilter(maxLength2))

        val status = resources.getStringArray(R.array.status_list)
        val arrayAdapter2 = ArrayAdapter(this, R.layout.dropdown_item_file, status)
        binding.autoCompleteTextView2.setAdapter(arrayAdapter2)

        binding.autoCompleteTextView2.setOnItemClickListener { parent, _, position, _ ->
            status2 = parent.getItemAtPosition(position).toString()
        }

        binding.staffContact.editTextName.inputType = InputType.TYPE_CLASS_NUMBER
        binding.staffPincode.editTextName.inputType = InputType.TYPE_CLASS_NUMBER
        binding.staffContact.editTextName.filters = arrayOf(InputFilter.LengthFilter(10))
        binding.staffPincode.editTextName.filters = arrayOf(InputFilter.LengthFilter(6))

        binding.autoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
            val selectedRole = parent.getItemAtPosition(position).toString()
            val roleId = roleMap[selectedRole] ?: -1
            roleType = roleId.toString()
            Toast.makeText(this, "Selected Role ID: $roleId", Toast.LENGTH_SHORT).show()
        }

        binding.addStaffBtn.setOnClickListener {
            validateAndSubmit()
        }

        binding.ivUpload.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_CODE_IMAGE)
        }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = data?.data
            if (imageUri != null) {
                selectedImageUri = imageUri
                selectedImageFile = File(getRealPathFromURI(this, imageUri)) // Convert to file

            }
        }
    }

    private fun getRealPathFromURI(context: Context, uri: Uri): String? {
        var result: String? = null
        val cursor: Cursor? = context.contentResolver.query(uri, arrayOf(MediaStore.Images.Media.DATA), null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                result = cursor.getString(columnIndex)
            }
            cursor.close()
        }
        return result
    }

    private fun fetchRolesFromApi() {
        val token = "Bearer ${sharedPrefManager.getToken()}"
        ApiClient.apiInterface.getAllRoles(token).enqueue(object : Callback<RoleResponse> {
            override fun onResponse(call: Call<RoleResponse>, response: Response<RoleResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val rolesList = response.body()!!.roles
                    roleMap.clear()
                    for (role in rolesList) {
                        roleMap[role.roleName] = role.roleId
                    }
                    setupDropdown(roleMap.keys.toList())
                } else {
                    Toast.makeText(this@AddStaffActivity, "Failed to load roles", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RoleResponse>, t: Throwable) {
                Toast.makeText(this@AddStaffActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupDropdown(roles: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)
        binding.autoCompleteTextView.setAdapter(adapter)
    }

    private fun validateAndSubmit() {
        val name = binding.staffName.textInputLayout.editText?.text.toString().trim()
        val contact = binding.staffContact.textInputLayout.editText?.text.toString().trim()
        val address = binding.staffAddress.textInputLayout.editText?.text.toString().trim()
        val country = binding.staffCountry.textInputLayout.editText?.text.toString().trim()
        val pincode = binding.staffPincode.textInputLayout.editText?.text.toString().trim()
        val city = binding.staffCity.textInputLayout.editText?.text.toString().trim()
        val email = binding.staffEmail.textInputLayout.editText?.text.toString().trim()
        val state = binding.staffState.textInputLayout.editText?.text.toString().trim()

        var isValid = true

        if (name.isEmpty()) { binding.staffName.textInputLayout.error = "Enter Staff Name"; isValid = false }
        if (contact.isEmpty() || contact.length != 10) { binding.staffContact.textInputLayout.error = "Enter a valid 10-digit Contact Number"; isValid = false }
        if (address.isEmpty()) { binding.staffAddress.textInputLayout.error = "Enter Staff Address"; isValid = false }
        if (country.isEmpty()) { binding.staffCountry.textInputLayout.error = "Enter Staff Country"; isValid = false }
        if (pincode.isEmpty() || pincode.length != 6) { binding.staffPincode.textInputLayout.error = "Enter a valid 6-digit PinCode"; isValid = false }
        if (city.isEmpty()) { binding.staffCity.textInputLayout.error = "Enter Staff City"; isValid = false }
        if (state.isEmpty()) { binding.staffState.textInputLayout.error = "Enter Staff State"; isValid = false }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) { binding.staffEmail.textInputLayout.error = "Enter a valid Email"; isValid = false }

        if (!isValid) return

//        addStaff()
    }

//    private fun addStaff() {
//        val token = sharedPrefManager.getToken()
//
//        val name = binding.staffName.textInputLayout.editText?.text.toString().trim()
//        val contact = binding.staffContact.textInputLayout.editText?.text.toString().trim()
//        val address = binding.staffAddress.textInputLayout.editText?.text.toString().trim()
//        val country = binding.staffCountry.textInputLayout.editText?.text.toString().trim()
//        val pincode = binding.staffPincode.textInputLayout.editText?.text.toString().trim()
//        val city = binding.staffCity.textInputLayout.editText?.text.toString().trim()
//        val email = binding.staffEmail.textInputLayout.editText?.text.toString().trim()
//        val state = binding.staffState.textInputLayout.editText?.text.toString().trim()
//
//        val photoPart = selectedImageFile?.let {
//            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), it)
//            MultipartBody.Part.createFormData("photo", it.name, requestFile)
//            binding.tvBrowse.text=it.name
//        }
//
//        ApiClient.apiInterface.addStaff(token.toString(), name, contact, address, "", city, pincode, state, country, email, roleType, photoPart)
//            .enqueue(object : Callback<CommonResponse> {
//                override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
//                    Toast.makeText(this@AddStaffActivity, response.body()?.message ?: "Upload Success", Toast.LENGTH_SHORT).show()
//                    finish()
//                }
//
//                override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
//                    Toast.makeText(this@AddStaffActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
//                }
//            })
//    }

    private fun getFilePart(uri: Uri, paramName: String): MultipartBody.Part {
        val file = File(uri.path ?: "")
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData(paramName, file.name, requestFile)
    }

    private fun getFileName(uri: Uri): String {
        return uri.lastPathSegment ?: "Selected Image"
    }
}
