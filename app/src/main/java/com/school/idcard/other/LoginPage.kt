package com.school.idcard.other

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.textfield.TextInputLayout
import com.school.idcard.R
import com.school.idcard.admin.AdminDashboardActivity
import com.school.idcard.agent.PremiumAgentDashboard
import com.school.idcard.databinding.ActivityLoginPageBinding
import com.school.idcard.network.ApiClient
import com.school.idcard.network.LoginRequest
import com.school.idcard.network.SharedPrefManager
import com.school.idcard.othermodel.LoginResponse
import com.school.idcard.superadmin.SuperAdminDashboard
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPage : AppCompatActivity() {

    private lateinit var binding:ActivityLoginPageBinding
    private lateinit var sharedPrefManager: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_login_page)
        sharedPrefManager=SharedPrefManager(this)

        binding.userNameEt.textInputLayout.hint="Registered Email Id"
        binding.passwordEt.textInputLayout.hint="Password"

        binding.passwordEt.textInputLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE






        binding.loginBtn.setOnClickListener {
            val userName= binding.userNameEt.textInputLayout.editText!!.text.trim().toString().replace(" ","")
            val password= binding.passwordEt.textInputLayout.editText!!.text.trim().toString().replace(" ","")


            if (userName.isEmpty()) {
                binding.userNameEt.textInputLayout.error = "Username cannot be empty"
                return@setOnClickListener
            } else {
                binding.userNameEt.textInputLayout.error = null  // Remove error if input is valid
            }

            // Check if password is empty
            if (password.isEmpty()) {
                binding.passwordEt.textInputLayout.error = "Password cannot be empty"
                return@setOnClickListener
            } else {
                binding.passwordEt.textInputLayout.error = null  // Remove error if input is valid
            }

            // Show Progress Animation and Disable Button
            binding.loaderLayout.visibility = View.VISIBLE
            binding.lottieProgress.playAnimation()
            binding.loginBtn.isEnabled = false

            loginFun(userName, password)

        }

        binding.registerdViaGoogle.setOnClickListener {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
        }

    }

    private fun loginFun(userName: String, password: String) {
        ApiClient.apiInterface.loginUser(LoginRequest(userName,password)).enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                if(response.isSuccessful){
                    binding.loaderLayout.visibility = View.GONE
                    binding.lottieProgress.pauseAnimation()
                    binding.loginBtn.isEnabled = true
                    if(response.body()!!.roleType=="SUPERADMIN"){
                        sharedPrefManager.saveToken(response.body()!!.token) // Save token in SharedPreferences
                        sharedPrefManager.saveRole(response.body()!!.roleType) // Save token in SharedPreferences
                        startActivity(Intent(this@LoginPage,SuperAdminDashboard::class.java))
                    }else if(response.body()!!.roleType=="PREMIUM" || response.body()!!.roleType=="NORMAL"  ){
                        sharedPrefManager.saveToken(response.body()!!.token) // Save token in SharedPreferences
                        sharedPrefManager.saveRole(response.body()!!.roleType) // Save token in SharedPreferences
                        startActivity(Intent(this@LoginPage,PremiumAgentDashboard::class.java))
                    }else if(response.body()!!.roleType=="ADMIN"){
                        sharedPrefManager.saveToken(response.body()!!.token) // Save token in SharedPreferences
                        sharedPrefManager.saveRole(response.body()!!.roleType) // Save token in SharedPreferences
                        startActivity(Intent(this@LoginPage,AdminDashboardActivity::class.java))
                    }else{
                        Toast.makeText(this@LoginPage,"Coming Soon", Toast.LENGTH_SHORT).show()
                    }

                }else{
                    binding.loaderLayout.visibility = View.GONE
                    binding.lottieProgress.pauseAnimation()
                    binding.loginBtn.isEnabled = true
                    Toast.makeText(this@LoginPage,response.message(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Hide Progress Animation and Enable Button on Failure
                binding.loaderLayout.visibility = View.GONE
                binding.lottieProgress.pauseAnimation()
                binding.loginBtn.isEnabled = true
                Toast.makeText(this@LoginPage,t.message,Toast.LENGTH_SHORT).show()
            }

        })
    }
}