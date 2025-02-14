package com.school.idcard.other

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.school.idcard.R
import com.school.idcard.databinding.ActivityLoginPageBinding
import com.school.idcard.network.LoginRequest
import com.school.idcard.network.SharedPrefManager
import com.school.idcard.othermodel.LoginResponse
import com.school.idcard.superadmin.SuperAdminDashboard
import com.school.idcard.network.ApiClient
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

        binding.loginBtn.setOnClickListener {
            val userName= binding.userNameEt.textInputLayout.editText!!.text.trim().toString()
            val password= binding.passwordEt.textInputLayout.editText!!.text.trim().toString()

            loginFun(userName, password)

        }

        binding.registerdViaGoogle.setOnClickListener {
            Toast.makeText(this,"Coming Soon",Toast.LENGTH_SHORT).show()
        }

    }

    private fun loginFun(userName: String, password: String) {
        ApiClient.apiInterface.loginUser(LoginRequest(userName,password)).enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful){
                    if(response.body()!!.roleType=="SUPERADMIN"){
                        sharedPrefManager.saveToken(response.body()!!.token) // Save token in SharedPreferences
                        startActivity(Intent(this@LoginPage,SuperAdminDashboard::class.java))
                    }else{
                        Toast.makeText(this@LoginPage,"Coming Soon", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginPage,t.message,Toast.LENGTH_SHORT).show()
            }

        })
    }
}