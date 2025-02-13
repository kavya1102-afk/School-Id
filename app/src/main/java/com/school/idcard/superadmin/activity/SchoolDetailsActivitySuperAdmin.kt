package com.school.idcard.superadmin.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.school.idcard.R
import com.school.idcard.databinding.ActivitySchoolDetailsSuperAdminBinding
import com.school.idcard.network.SharedPrefManager
import com.school.idcard.superadmin.fragment.SchoolStaffSuperFragment
import com.school.idcard.superadmin.fragment.SchoolStudentSuperFragment

class SchoolDetailsActivitySuperAdmin : AppCompatActivity() {

    private lateinit var binding:ActivitySchoolDetailsSuperAdminBinding
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var schoolId:String

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_school_details_super_admin)

        binding.toolbar.fileName.text="Submitted For Print"
        binding.toolbar.backArrowBtn.setOnClickListener { finish() }
        schoolId= intent.getStringExtra("schoolId").toString()

        val adapter = TabAdapter2(this, schoolId) // ✅ Use requireActivity()
        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 1


        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = if (position == 0) "School Student" else "School Staff"
        }.attach()

    }
}

class TabAdapter2(fragmentActivity: FragmentActivity,private val selectedSchoolId:String) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SchoolStudentSuperFragment.newInstance(selectedSchoolId)
            1 -> SchoolStaffSuperFragment.newInstance(selectedSchoolId)
            else -> SchoolStudentSuperFragment.newInstance(selectedSchoolId)
        }
    }
}
