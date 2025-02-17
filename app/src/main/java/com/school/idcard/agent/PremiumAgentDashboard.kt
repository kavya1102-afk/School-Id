package com.school.idcard.agent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.school.idcard.R
import com.school.idcard.agent.fragment.PremiumHomeFragment
import com.school.idcard.agent.fragment.PremiumProfileFragment
import com.school.idcard.agent.fragment.PremiumSettingFragment
import com.school.idcard.databinding.ActivityPremiumAgentDashboardBinding
import com.school.idcard.superadmin.fragment.SchoolListFragment
import com.school.idcard.superadmin.fragment.SuperAdminHomeFragment

class PremiumAgentDashboard : AppCompatActivity() {

    private lateinit var binding:ActivityPremiumAgentDashboardBinding
    private val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this, R.layout.activity_premium_agent_dashboard)

        binding.bottomNavigation.selectedItemId = R.id.nav_home

        setupClickListener()

        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.frameLayout, PremiumHomeFragment())
        fragmentTransaction.commit()

        loadFragment(SuperAdminHomeFragment())


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    private fun setupClickListener() {
        binding.bottomNavigation.setOnItemSelectedListener {
            val fragment: Fragment = when (it.itemId) {
                R.id.nav_setting -> PremiumSettingFragment()
                R.id.nav_school -> SchoolListFragment()
                R.id.nav_profile -> PremiumProfileFragment()
                else -> PremiumHomeFragment()
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