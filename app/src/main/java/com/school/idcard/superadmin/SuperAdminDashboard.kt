package com.school.idcard.superadmin

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.school.idcard.R
import com.school.idcard.superadmin.adapter.super_details_card_adapter
import com.school.idcard.databinding.ActivitySuperAdminDashboardBinding
import com.school.idcard.superadmin.fragment.SchoolListFragment
import com.school.idcard.superadmin.fragment.SuperAdminAgentFragment
import com.school.idcard.superadmin.fragment.SuperAdminHomeFragment
import com.school.idcard.superadmin.fragment.SuperadminProfileFragment
import com.school.idcard.superadmin.model.CardDetailsModel

@Suppress("IMPLICIT_CAST_TO_ANY")
class SuperAdminDashboard : AppCompatActivity() {

    private lateinit var binding: ActivitySuperAdminDashboardBinding
    private val fragmentManager = supportFragmentManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_super_admin_dashboard)
        binding.bottomNavigation.selectedItemId = R.id.nav_home

        setupClickListener()

        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.frameLayout,SuperAdminHomeFragment())
        fragmentTransaction.commit()

        loadFragment(SuperAdminHomeFragment())


        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }

    private fun setupClickListener() {
        binding.bottomNavigation.setOnItemSelectedListener {
            val fragment: Fragment = when (it.itemId) {
                R.id.nav_bt2 -> SuperAdminAgentFragment()
                R.id.nav_school -> SchoolListFragment()
                R.id.nav_profile -> SuperadminProfileFragment()
                else -> SuperAdminHomeFragment()
            }

            loadFragment(fragment)
            true
        }
    }


    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}