package com.school.idcard.other

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.textfield.TextInputLayout
import com.school.idcard.R
import com.school.idcard.databinding.ActivityChangePasswordBinding
import com.school.idcard.network.ApiClient
import com.school.idcard.network.CommonResponse
import com.school.idcard.network.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding:ActivityChangePasswordBinding
    private lateinit var sharedPrefManager: SharedPrefManager

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this, R.layout.activity_change_password)
        sharedPrefManager=SharedPrefManager(this)

        binding.backArrowBtn.setOnClickListener {
            finish()
        }


        binding.newPasswordEt.textInputLayout.hint="New Password"
        binding.oldPasswordEt.textInputLayout.hint="Old Password"
        binding.confirmPasswordEt.textInputLayout.hint="Confirm Password"

        binding.oldPasswordEt.textInputLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
        binding.newPasswordEt.textInputLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
        binding.confirmPasswordEt.textInputLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE



        binding.changePasswordBtn.setOnClickListener {
            val oldPassword = binding.oldPasswordEt.textInputLayout.editText!!.text.toString().trim()
            val newPassword = binding.newPasswordEt.textInputLayout.editText!!.text.toString().trim()
            val confirmPassword = binding.confirmPasswordEt.textInputLayout.editText!!.text.toString().trim()

            when {
                oldPassword.isEmpty() -> {
                    binding.oldPasswordEt.textInputLayout.error = "Old password is required"
                }

                newPassword.isEmpty() -> {
                    binding.newPasswordEt.textInputLayout.error = "New password is required"
                }

                newPassword.length < 6 -> {
                    binding.newPasswordEt.textInputLayout.error = "Password must be at least 6 characters"
                }

                newPassword == oldPassword -> {
                    binding.newPasswordEt.textInputLayout.error = "New password should be different from old password"
                }

                confirmPassword.isEmpty() -> {
                    binding.confirmPasswordEt.textInputLayout.error = "Confirm password is required"
                }

                confirmPassword != newPassword -> {
                    binding.confirmPasswordEt.textInputLayout.error = "Passwords do not match"
                }

                else -> {

                    changePassword(oldPassword, newPassword)  

                }
            }
        }



    }

    private fun changePassword(oldPassword: String, newPassword: String) {
        val token="Bearer ${sharedPrefManager.getToken()}"

        ApiClient.apiInterface.changePassword(token.toString(), oldPassword,newPassword).enqueue(object : Callback<CommonResponse>{
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if(response.isSuccessful){
                    Toast.makeText(this@ChangePasswordActivity,response.body()!!.message, Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(this@ChangePasswordActivity,response.body()!!.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Toast.makeText(this@ChangePasswordActivity,t.message, Toast.LENGTH_SHORT).show()
            }
        })

    }
}