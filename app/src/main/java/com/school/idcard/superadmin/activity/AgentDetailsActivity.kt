package com.school.idcard.superadmin.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.school.idcard.R
import com.school.idcard.databinding.ActivityAgentDetailsBinding
import com.school.idcard.superadmin.fragment.SchoolListFragment

class AgentDetailsActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAgentDetailsBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_agent_details)

        binding.toolbar.backArrowBtn.setOnClickListener { finish() }
        binding.toolbar.fileName.text="Agent Info"

        binding.editIconBtn.setOnClickListener {
            startActivity(Intent(this, AddAgentActivity::class.java).putExtra("type", 1))
        }


    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }

}