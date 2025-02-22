package com.school.idcard.admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.school.idcard.R
import com.school.idcard.databinding.ActivityAdminSideMenuBinding

class AdminSideMenuActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAdminSideMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_admin_side_menu)

        binding.backArrowBtn.setOnClickListener { finish() }

        binding.homeSide.setOnClickListener {
            startActivity(Intent(this,AdminSideMenuActivity::class.java))
            finish()
        }

    }
}