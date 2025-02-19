package com.school.idcard.superadmin.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.school.idcard.R
import com.school.idcard.databinding.ActivitySuperAdminSideMenuBinding
import com.school.idcard.network.SharedPrefManager
import com.school.idcard.other.ChangePasswordActivity
import com.school.idcard.other.LoginPage
import com.school.idcard.superadmin.SuperAdminDashboard

class SuperAdminSideMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySuperAdminSideMenuBinding
    private lateinit var sharedPrefManager: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_super_admin_side_menu)
        sharedPrefManager = SharedPrefManager(this)

        binding.backArrowBtn.setOnClickListener { finish() }


        binding.homeSide.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    SuperAdminDashboard::class.java
                )
            )
            finish()
        }

        binding.addAgentSide.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    AddAgentActivity::class.java
                )
            )
            finish()
        }

        binding.addSchoolSide.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    AddSchoolActivity::class.java
                )
            )
            finish()
        }

        binding.changePasswordSide.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    ChangePasswordActivity::class.java
                )
            )
            finish()
        }

        binding.logoutBtn.setOnClickListener {
            sharedPrefManager.clear()
            startActivity(Intent(this, LoginPage::class.java))
            finish()
        }


    }
}