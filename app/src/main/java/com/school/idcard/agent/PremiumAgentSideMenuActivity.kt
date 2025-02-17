package com.school.idcard.agent

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.school.idcard.R
import com.school.idcard.databinding.ActivityPremiumAgentSideMenuBinding
import com.school.idcard.network.SharedPrefManager
import com.school.idcard.other.ChangePasswordActivity
import com.school.idcard.other.LoginPage
import com.school.idcard.superadmin.activity.AddSchoolActivity

class PremiumAgentSideMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPremiumAgentSideMenuBinding
    private lateinit var sharedPrefManager: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_premium_agent_side_menu)
        sharedPrefManager = SharedPrefManager(this)

        binding.homeSide.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    PremiumAgentDashboard::class.java
                )
            )
            finish()
        }

        val role=sharedPrefManager.getRole()

        if(role=="NORMAL"){
            binding.addSchoolSide.visibility=View.GONE
        }else{
            binding.addSchoolSide.visibility=View.VISIBLE
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